package net.team42.valuefiller.example;

import net.team42.valuefiller.ValueFillerActivator;
import net.team42.valuefiller.example.vo.TestVO;

public class Main {
	public static void main(String[] args) {
		TestVO testVO = new TestVO();
		ValueFillerActivator.fill(testVO);
		System.out.println("-file");
		System.out.println(testVO.getFileTestContent());
		System.out.println("-http");
		System.out.println(testVO.getHttpTestContent());
	}
}
