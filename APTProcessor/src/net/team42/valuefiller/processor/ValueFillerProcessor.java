package net.team42.valuefiller.processor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import net.team42.valuefiller.FillInvoker;
import net.team42.valuefiller.ValueFillerHelper;
import net.team42.valuefiller.annotation.ValueFiller;

public class ValueFillerProcessor extends AbstractProcessor {

	private Elements elementUtils;
	private Types typeUtils;
	private Filer filer;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.filer = processingEnv.getFiler();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		PrintWriter pw;
		try {
			Map<String, Map<String, ValueFiller>> classMap = new HashMap<String, Map<String, ValueFiller>>();
			for (TypeElement typeElement : annotations) {

				System.out.println("Type Eleement : " + typeElement.getSimpleName());
				for (Element element : env.getElementsAnnotatedWith(typeElement)) {
					String className = element.getEnclosingElement().toString();
					Map<String, ValueFiller> currentClassMap;
					System.out.println("VALUEFILLER : " + typeElement.getAnnotation(ValueFiller.class));
					if ((currentClassMap = classMap.get(className)) != null) {
						currentClassMap.put(element.toString(), element.getAnnotation(ValueFiller.class));
					} else {
						currentClassMap = new HashMap<String, ValueFiller>();
						classMap.put(className, currentClassMap);
						currentClassMap.put(element.toString(), element.getAnnotation(ValueFiller.class));
					}
				}
			}


			for (String className : classMap.keySet()) {
				com.squareup.javapoet.TypeSpec.Builder activatorBuilder = TypeSpec.classBuilder("FillInvokerImpl$$"+className.substring(className.lastIndexOf('.')+1)).addModifiers(Modifier.PUBLIC);
				activatorBuilder.superclass(ClassName.bestGuess(FillInvoker.class.getCanonicalName()));
				activatorBuilder.addMethod(MethodSpec.methodBuilder("fill").addModifiers(Modifier.PUBLIC).returns(Void.TYPE).addParameter(TypeName.OBJECT, "fillClass").addStatement("this.fill(($L)fillClass)", className).build());
				Builder fillMethodBuilder = MethodSpec.methodBuilder("fill").addModifiers(Modifier.PUBLIC).returns(Void.TYPE).addParameter(ClassName.bestGuess(className), "fillClass");
				Map<String, ValueFiller> currentClassMap = classMap.get(className);
				for (String fieldName : currentClassMap.keySet()) {
					ValueFiller currentOption = currentClassMap.get(fieldName);
					System.out.println("CURRENT OPTION : " + currentOption);
					fillMethodBuilder.addStatement("fillClass.$L = $T.$L($S,$S)", fieldName, ValueFillerHelper.class, (currentOption.sourceType() == ValueFiller.SOURCETYPE_FILE ? ("findFileContent") : ("findHTTPContent")),
							currentOption.path(), currentOption.charset());
				}
				activatorBuilder.addMethod(fillMethodBuilder.build());
				try {
					JavaFile.builder(className.substring(0,className.lastIndexOf('.')), activatorBuilder.build()).build().writeTo(filer);
				} catch (IOException e) {
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> supportedAnnotationTypes = new HashSet<String>();
		supportedAnnotationTypes.add(ValueFiller.class.getCanonicalName());

		return supportedAnnotationTypes;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}
}