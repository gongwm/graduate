package hust.hx.util.dao;

import java.io.IOException;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.UncategorizedDataAccessException;

public class ExceptionHandler {
	private String errorMessage = null;

	public ExceptionHandler(DaoRunner runner) {
		try {
			runner.runDao();
		} catch (DataAccessResourceFailureException e) {
			errorMessage = "数据库资源无法访问：" + e.getMessage();
			e.printStackTrace();
		} catch (ConcurrencyFailureException e) {
			errorMessage = "并发访问失败：" + e.getMessage();
		} catch (InvalidDataAccessApiUsageException e) {
			errorMessage = "数据访问api使用错误：" + e.getMessage();
		} catch (DataRetrievalFailureException e) {
			errorMessage = "数据获取异常：" + e.getMessage();
		} catch (UncategorizedDataAccessException e) {
			errorMessage = "其他数据库异常：" + e.getMessage();
		} catch (IllegalStateException e) {
			if (e.getCause() instanceof IOException) {
				errorMessage = "输入输出错误：" + e.getCause().getMessage();
			}
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		if (errorMessage != null) {
			errorMessage += "。";
		}
	}

	public static String trialAndError(DaoRunner runner) {
		return new ExceptionHandler(runner).getErrorMessage();
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public interface DaoRunner {
		public void runDao();
	}

}
