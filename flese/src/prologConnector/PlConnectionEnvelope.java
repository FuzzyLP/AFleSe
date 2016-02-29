package prologConnector;

import java.io.IOException;
import java.util.ArrayList;

import logs.LogsManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import CiaoJava.PLConnection;
import CiaoJava.PLException;
import CiaoJava.PLGoal;
import CiaoJava.PLTerm;
import CiaoJava.PLVariable;
import auxiliar.InterruptTimerTask;
import auxiliar.LocalUserInfo;
import constants.KConstants;
import filesAndPaths.FilesAndPathsException;
import filesAndPaths.PathsMgmt;

public class PlConnectionEnvelope {

	final static private Log LOG = LogFactory.getLog(PlConnectionEnvelope.class);

	private static PlConnectionEnvelopeData createPlConnection(LocalUserInfo localUserInfo)
			throws PlConnectionEnvelopeException, FilesAndPathsException {
		PlConnectionEnvelopeData data = new PlConnectionEnvelopeData(localUserInfo);
		PathsMgmt pathsMgmt = new PathsMgmt();

		String[] argv = new String[1];
		argv[0] = pathsMgmt.getPlServerPath();

		try {
			data.setPlConnection(new PLConnection(argv));
		} catch (IOException e) {
			e.printStackTrace();
			throw new PlConnectionEnvelopeException("IOException: " + e.getMessage());
		} catch (PLException e) {
			e.printStackTrace();
			throw new PlConnectionEnvelopeException("PLException: " + e.getMessage());
		}
		return data;
	}

	private static void destroyPlConnection(PlConnectionEnvelopeData data)
			throws PlConnectionEnvelopeException {
		if ((data != null) && (data.getPlConnection() != null)) {
			try {
				data.getPlConnection().stop();
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new PlConnectionEnvelopeException("InterruptedException: " + e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				throw new PlConnectionEnvelopeException("IOException: " + e.getMessage());
			} catch (PLException e) {
				e.printStackTrace();
				throw new PlConnectionEnvelopeException("PLException: " + e.getMessage());
			}
		}
	}

	public static synchronized void runPrologQuery(CiaoPrologQueryInterface query, LocalUserInfo localUserInfo)
			throws PlConnectionEnvelopeException, CiaoPrologConnectorException, FilesAndPathsException {

		// RunCommand.run("killall -q ciaoengine.LINUXi86_64 ciaoengine.LINUXi86 ciaoengine", LOG);
		InterruptTimerTask.getInstance(Thread.currentThread()).reschedule(0);
		try {
			runPrologQueryWithTimer(query, localUserInfo);
		} catch (InterruptedException e) {
			throw new PlConnectionEnvelopeException("PLConnectionException: timeout exeeded");
		} finally {
			InterruptTimerTask.getInstance(Thread.currentThread()).deactivate();
		}
	}

	private static void runPrologQueryWithTimer(CiaoPrologQueryInterface query, LocalUserInfo localUserInfo)
			throws PlConnectionEnvelopeException, CiaoPrologConnectorException, FilesAndPathsException,
			InterruptedException {
		PlConnectionEnvelopeData data = null;
		try {
			data = createPlConnection(localUserInfo);
			InterruptTimerTask.getInstance(Thread.currentThread()).reschedule(0);
			changeCiaoPrologWorkingFolder(query, data);
			InterruptTimerTask.getInstance(Thread.currentThread()).reschedule(0);
			runPrologQueryAux(query, data);
		} finally {
			destroyPlConnection(data);
		}
	}

	private static void changeCiaoPrologWorkingFolder(CiaoPrologQueryInterface query,
			PlConnectionEnvelopeData data) throws CiaoPrologConnectorException, FilesAndPathsException,
			PlConnectionEnvelopeException, InterruptedException {

		CiaoPrologQueryInterface folderChangeQuery = CiaoPrologChangeWorkingFolderQuery.getInstance(query
				.getProgramFileInfo());
		runPrologQueryAux(folderChangeQuery, data);

		CiaoPrologQueryInterface folderCheckQuery = CiaoPrologCheckWorkingFolderQuery.getInstance(query
				.getProgramFileInfo());
		runPrologQueryAux(folderCheckQuery, data);
	}

	private static void runPrologQueryAux(CiaoPrologQueryInterface query, PlConnectionEnvelopeData data)
			throws PlConnectionEnvelopeException, CiaoPrologConnectorException, InterruptedException,
			FilesAndPathsException {
		LOG.info(query.getQuery().toString());
		if (data.getPlConnection() == null) {
			throw new PlConnectionEnvelopeException("ERROR: plConnection is null.");
		}

		createGoal(query, data);
		if (!query.isOfType(CiaoPrologQueryAbstract.Constants.ChangeWorkingFolderQuery)) {
			// Thread.sleep(1000);
			changeProgramFileTo(query, data);
		}
		// Thread.sleep(1000);
		evaluateGoal(data);

		long answersCounter = 0;

		LOG.info("performQueryAux: getting answers ... ");
		PLTerm prologQueryAnswer;
		long timesCounter;
		boolean queryIsStillRunning = true;

		ArrayList<CiaoPrologQueryAnswer> ciaoPrologQueryAnswers = new ArrayList<CiaoPrologQueryAnswer>();

		StringBuilder msgsAccumulator = new StringBuilder();
		do { // Get all the answers you can.
			prologQueryAnswer = null;
			timesCounter = 0;
			// Save the current answer.
			answersCounter++;
			msgsAccumulator.append("getting answer number: " + answersCounter + "\n");
			// LOG.info(msgsAccumulator);
			// msgsAccumulator = "";

			do { // Get the current answer.
				prologQueryAnswer = getNextSolution(data);
				queryIsStillRunning = getQueryIsStillRunning(data);
				timesCounter++;
				InterruptTimerTask.getInstance(Thread.currentThread()).reschedule(0);

			} while ((prologQueryAnswer == null) && queryIsStillRunning
					&& (timesCounter < KConstants.CiaoPrologQuery.maximumNumberOfRetries));

			if (timesCounter >= KConstants.CiaoPrologQuery.maximumNumberOfRetries) {
				LOG.info("performQueryAux: reached maxNumberOfTries: " + timesCounter + " >= "
						+ KConstants.CiaoPrologQuery.maximumNumberOfRetries);
			}

			msgsAccumulator.append("goal: " + data.getEvaluatedGoal().toString() + "\n");
			if (prologQueryAnswer != null) {
				CiaoPrologQueryAnswer ciaoPrologQueryAnswer = new CiaoPrologQueryAnswer();
				int variablesLength = query.getVariablesLength();
				for (int i = 0; i < variablesLength; i++) {
					msgsAccumulator.append("      var[" + i + "]: ");

					PLVariable variable = query.getVariables()[i];
					String variableName = query.getVariablesNames()[i];
					CiaoPrologTermInJava ciaoPrologTermInJava = null;

					if (variable != null) {
						msgsAccumulator.append(variable.toString());
						msgsAccumulator.append(" bind: ");
						msgsAccumulator.append(variable.getBinding());
						ciaoPrologTermInJava = new CiaoPrologTermInJava(variable, prologQueryAnswer);
						msgsAccumulator.append(" -> " + ciaoPrologTermInJava.toString() + " \n");
					} else {
						ciaoPrologTermInJava = null;
						msgsAccumulator.append("null -> null \n");
					}

					ciaoPrologQueryAnswer.addCiaoPrologVariableAnswer(variableName, ciaoPrologTermInJava);
					InterruptTimerTask.getInstance(Thread.currentThread()).reschedule(0);
				}
				ciaoPrologQueryAnswers.add(ciaoPrologQueryAnswer);
				// LOG.info(msgsAccumulator);
			} else {
				// LOG.info("performQueryAux: answer obtained: null ");
				msgsAccumulator.append("answer obtained: null \n");
			}

			InterruptTimerTask.getInstance(Thread.currentThread()).reschedule(0);

		} while ((prologQueryAnswer != null)
				&& (answersCounter < KConstants.CiaoPrologQuery.maximumNumberOfAnswers));

		InterruptTimerTask.getInstance(Thread.currentThread()).deactivate();

		CiaoPrologQueryAnswer[] ciaoPrologQueryAnswersArray = ciaoPrologQueryAnswers
				.toArray(new CiaoPrologQueryAnswer[ciaoPrologQueryAnswers.size()]);
		query.setQueryAnswers(ciaoPrologQueryAnswersArray);

		LOG.info(msgsAccumulator.toString());
		// LOG.info("performQueryAux: terminating goal execution ...");
		if (data.getEvaluatedGoal() != null) {
			try {
				data.getEvaluatedGoal().terminate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			data.setEvaluatedGoal(null);
			answersCounter = -1; // Notify that there is no currentGoal.
		}

		// LOG.info("performQueryAux: end.");

	}

	private static void createGoal(CiaoPrologQueryInterface query, PlConnectionEnvelopeData data)
			throws PlConnectionEnvelopeException, CiaoPrologConnectorException {
		LOG.info("runQuery: creating goal for query: " + query.toString() + " .... ");
		LogsManager.logQuery(query, data.getLocalUserInfo());
		PLGoal evaluatedGoal = new PLGoal(data.getPlConnection(), query.getQuery());
		data.setEvaluatedGoal(evaluatedGoal);
	}

	private static void changeProgramFileTo(CiaoPrologQueryInterface query, PlConnectionEnvelopeData data)
			throws PlConnectionEnvelopeException, CiaoPrologConnectorException, FilesAndPathsException {
		query.getProgramFileInfo().ensureFolderIsAccessibleAndCleanCiaoPrologTmpFiles();

		String programFileName = query.getProgramFileInfo().getFileName();
		LOG.info("runQuery: changing programFile to: " + programFileName + " .... ");
		try {
			data.getEvaluatedGoal().useModule(programFileName);
			LOG.info("runQuery: changed programFile to: " + programFileName + " .... ");
		} catch (IOException e) {
			e.printStackTrace();
			throw new PlConnectionEnvelopeException("IOException");
		} catch (PLException e) {
			e.printStackTrace();
			throw new PlConnectionEnvelopeException("PLException");
		}
	}

	private static void evaluateGoal(PlConnectionEnvelopeData data) throws PlConnectionEnvelopeException,
			CiaoPrologConnectorException {
		LOG.info("runQuery: executing query .... ");
		try {
			data.getEvaluatedGoal().query();
		} catch (IOException e) {
			e.printStackTrace();
			throw new PlConnectionEnvelopeException("IOException");
		} catch (PLException e) {
			e.printStackTrace();
			throw new PlConnectionEnvelopeException("PLException");
		}
	}

	private static PLTerm getNextSolution(PlConnectionEnvelopeData data) throws PlConnectionEnvelopeException {
		PLTerm prologQueryAnswer = null;
		try {
			prologQueryAnswer = data.getEvaluatedGoal().nextSolution();
		} catch (IOException e) {
			e.printStackTrace();
			throw new PlConnectionEnvelopeException("IOException");
		} catch (PLException e) {
			e.printStackTrace();
			throw new PlConnectionEnvelopeException("PLException");
		}
		return prologQueryAnswer;
	}

	private static boolean getQueryIsStillRunning(PlConnectionEnvelopeData data)
			throws PlConnectionEnvelopeException {
		boolean queryIsStillRunning = false;
		try {
			queryIsStillRunning = data.getEvaluatedGoal().isStillRunning();
		} catch (IOException e) {
			e.printStackTrace();
			throw new PlConnectionEnvelopeException("IOException");
		} catch (PLException e) {
			e.printStackTrace();
			throw new PlConnectionEnvelopeException("PLException");
		}
		return queryIsStillRunning;
	}

}
