package clonegod.spring.framework.web.servlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import clonegod.spring.framework.web.servlet.mvc.ModelAndView;

public class HandlerExecutionChain {

	private final Object handler; // controller

	private List<HandlerInterceptor> interceptorList;

	private int interceptorIndex = -1;


	/**
	 * Create a new HandlerExecutionChain.
	 * @param handler the handler object to execute
	 */
	public HandlerExecutionChain(Object handler) {
		this(handler, (HandlerInterceptor[]) null);
	}

	/**
	 * Create a new HandlerExecutionChain.
	 * @param handler the handler object to execute
	 * @param interceptors the array of interceptors to apply
	 * (in the given order) before the handler itself executes
	 */
	public HandlerExecutionChain(Object handler, HandlerInterceptor... interceptors) {
		this.interceptorList = new ArrayList<HandlerInterceptor>();
		if (handler instanceof HandlerExecutionChain) {
			HandlerExecutionChain originalChain = (HandlerExecutionChain) handler;
			this.handler = originalChain.getHandler();
		}
		else {
			this.handler = handler;
			if(interceptors == null) {
				interceptors = new HandlerInterceptor[] {new HandlerInterceptor() {} };
			}
			this.interceptorList.addAll(Arrays.asList(interceptors));
		}
	}


	/**
	 * Return the handler object to execute.
	 * @return the handler object
	 */
	public Object getHandler() {
		return this.handler;
	}

	public void addInterceptor(HandlerInterceptor interceptor) {
		initInterceptorList().add(interceptor);
	}

	public void addInterceptors(HandlerInterceptor... interceptors) {
		if (interceptors != null) {
			initInterceptorList().addAll(Arrays.asList(interceptors));
		}
	}

	private List<HandlerInterceptor> initInterceptorList() {
		if (this.interceptorList == null) {
			this.interceptorList = new ArrayList<HandlerInterceptor>();
		}
		return this.interceptorList;
	}

	/**
	 * Return the array of interceptors to apply (in the given order).
	 * @return the array of HandlerInterceptors instances (may be {@code null})
	 */
	public List<HandlerInterceptor> getInterceptors() {
		return this.interceptorList;
	}


	/**
	 * Apply preHandle methods of registered interceptors.
	 * @return {@code true} if the execution chain should proceed with the
	 * next interceptor or the handler itself. Else, DispatcherServlet assumes
	 * that this interceptor has already dealt with the response itself.
	 */
	boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<HandlerInterceptor> interceptors = getInterceptors();
		if (interceptors != null) {
			for (int i = 0; i < interceptors.size(); i++) {
				HandlerInterceptor interceptor = interceptors.get(i);
				if (!interceptor.preHandle(request, response, this.handler)) {
					triggerAfterCompletion(request, response, null);
					return false;
				}
				this.interceptorIndex = i;
			}
		}
		return true;
	}

	/**
	 * Apply postHandle methods of registered interceptors.
	 */
	void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
		List<HandlerInterceptor> interceptors = getInterceptors();
		if (interceptors != null) {
			for (int i = interceptors.size() - 1; i >= 0; i--) {
				HandlerInterceptor interceptor = interceptors.get(i);
				interceptor.postHandle(request, response, this.handler, mv);
			}
		}
	}

	/**
	 * Trigger afterCompletion callbacks on the mapped HandlerInterceptors.
	 * Will just invoke afterCompletion for all interceptors whose preHandle invocation
	 * has successfully completed and returned true.
	 */
	void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception ex)
			throws Exception {

		List<HandlerInterceptor> interceptors = getInterceptors();
		if (interceptors != null) {
			for (int i = this.interceptorIndex; i >= 0; i--) {
				HandlerInterceptor interceptor = interceptors.get(i);
				try {
					interceptor.afterCompletion(request, response, this.handler, ex);
				}
				catch (Throwable ex2) {
					ex2.printStackTrace();
				}
			}
		}
	}


}
