package annotation;

/**
 * Desc:
 * Created by Chiclaim on 2018/10/15.
 */
public class JavaBean {
    //@PropertyOnly Kotlin定义的Property不能使用到Java属性上，可以使用AnnotationTarget.FIELD
    private String id;

    @FiledOnly
    private String name;

}
