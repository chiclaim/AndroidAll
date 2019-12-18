package com.chiclaim.butterknife.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * 保存注解上的相关信息,用于生成Binding类
 * Created by chiclaim on 2016/09/23
 */
final class BindClass {

    private final ClassName UTILS = ClassName.get("com.chiclaim.butterknife", "ProcessorUtils");
    private final ClassName VIEW = ClassName.get("android.view", "View");


    private TypeName targetTypeName;
    private ClassName bindingClassName;
    private boolean isFinal;
    /**
     * 一个类中可能有多处使用了注解
     */
    private List<ViewBinding> fields;

    private BindClass(TypeElement enclosingElement) {
        //asType 表示注解所在字段是什么类型(eg. Button TextView)
        TypeName targetType = TypeName.get(enclosingElement.asType());
        if (targetType instanceof ParameterizedTypeName) {
            targetType = ((ParameterizedTypeName) targetType).rawType;
        }
        String packageName = enclosingElement.getQualifiedName().toString();
        packageName = packageName.substring(0, packageName.lastIndexOf("."));
        //String className = enclosingElement.getQualifiedName().toString().substring(packageName.length() + 1).replace('.', '$');
        String className = enclosingElement.getSimpleName().toString();
        ClassName bindingClassName = ClassName.get(packageName, className + "_ViewBinding");

        boolean isFinal = enclosingElement.getModifiers().contains(Modifier.FINAL);
        this.targetTypeName = targetType;
        this.bindingClassName = bindingClassName;
        this.isFinal = isFinal;
        fields = new ArrayList<>();
    }

    static BindClass createBindClass(TypeElement enclosingElement) {
        return new BindClass(enclosingElement);
    }

    ClassName getBindingClassName() {
        return bindingClassName;
    }

    public void setBindingClassName(ClassName bindingClassName) {
        this.bindingClassName = bindingClassName;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public TypeName getTargetTypeName() {
        return targetTypeName;
    }

    public void setTargetTypeName(TypeName targetTypeName) {
        this.targetTypeName = targetTypeName;
    }

    public Collection<ViewBinding> getFields() {
        return fields;
    }

    public void setFields(List<ViewBinding> fields) {
        this.fields = fields;
    }


    void addAnnotationField(ViewBinding viewBinding) {
        fields.add(viewBinding);
    }


    JavaFile preJavaFile() {
        return JavaFile.builder(bindingClassName.packageName(), createTypeSpec())
                .addFileComment("Generated code from My Butter Knife. Do not modify!!!")
                .build();
    }

    private TypeSpec createTypeSpec() {
        TypeSpec.Builder result = TypeSpec.classBuilder(bindingClassName.simpleName())
                .addModifiers(PUBLIC);
        if (isFinal) {
            result.addModifiers(FINAL);
        }

        result.addMethod(createConstructor(targetTypeName));


        return result.build();
    }


    private MethodSpec createConstructor(TypeName targetType) {
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC);

        constructor.addParameter(targetType, "target", FINAL);
        constructor.addParameter(VIEW, "source");

        //if (hasTargetField()) {
        //    constructor.addStatement("this.target = target");
        //    constructor.addCode("\n");
        //}

        for (ViewBinding bindings : fields) {
            addViewBinding(constructor, bindings);
        }

        return constructor.build();
    }


    private void addViewBinding(MethodSpec.Builder result, ViewBinding binding) {
        // Optimize the common case where there's a single binding directly to a field.
        //FieldViewBinding fieldBinding = bindings.getFieldBinding();
        CodeBlock.Builder builder = CodeBlock.builder()
                .add("target.$L = ", binding.getName());

        boolean requiresCast = requiresCast(binding.getType());
        if (!requiresCast) {
            builder.add("source.findViewById($L)", binding.getValue());
        } else {
            builder.add("$T.findViewByCast", UTILS);
            //builder.add(fieldBinding.isRequired() ? "RequiredView" : "OptionalView");
            //if (requiresCast) {
            //    builder.add("AsType");
            //}
            builder.add("(source, $L", binding.getValue());
            builder.add(", $T.class", binding.getRawType());
            builder.add(")");
        }
        result.addStatement("$L", builder.build());

    }


    private static boolean requiresCast(TypeName type) {
        return !"android.view.View".equals(type.toString());
    }

}
