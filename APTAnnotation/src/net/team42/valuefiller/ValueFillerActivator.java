package net.team42.valuefiller;

import java.util.HashMap;
import java.util.Map;

public class ValueFillerActivator {

	private static Map<Class, FillInvoker> invokerMap = new HashMap<Class, FillInvoker>();

	public static void fill(Object target) {
		FillInvoker invoker;
		try {
			if ((invoker = invokerMap.get(target.getClass())) != null) {
				invoker.fill(target);
			} else {
				invoker = (FillInvoker) Class.forName(target.getClass().getPackage().getName() + "." + "FillInvokerImpl$$"+target.getClass().getSimpleName()).newInstance();
				invokerMap.put(target.getClass(), invoker);
				invoker.fill(target);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(target.getClass().getPackage().getName() + "." + "FillInvokerImpl$$"+target.getClass().getSimpleName());
		}
		// try {
		// if (fillInvoker == null) {
		// fillInvoker = (FillInvoker)
		// Class.forName("net.team42.valuefiller.FillInvokerImpl").newInstance();
		// }
		// fillInvoker.fill(target);
		// } catch (Exception e) {
		// }
	}
}
