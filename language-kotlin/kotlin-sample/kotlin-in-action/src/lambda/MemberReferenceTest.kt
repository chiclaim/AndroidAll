package lambda


/**
 * desc: Lambda member reference (variable、method)
 *
 * Created by Chiclaim on 2018/09/22
 */

class MemberReference {

    fun variableReference() {

        val age = Person::age

        //和上面功能是一样的
        val age2 = { person: Person -> person.age }

        val ageValue = age(Person("yuzhiqiang", 18))
        val ageValue2 = age2(Person("chiclaim", 18))

    }

    fun sendEmail(person: Person, message: String) {
        println("send email...")
    }

    fun functionReference() {

        //通过匿名内部类(Function2)实现，在我们LambdaToVariableTest例子中底层不是通过匿名内部类
        //为啥要通过内部类？因为lambda表达式体里使用到了外面的sendEmail方法
        //如果表达式里对外没有任何引用，则底层不需要通过内部类来实现
        val action0 = { person: Person, message: String ->
            sendEmail(person, message)

            //如果仅有下面一句println，则底层不需要通过内部类来实现
            //println("${person.name} send email")
        }

        //和上面是等价的
        val action1 = ::sendEmail

        action0(Person("chiclaim", 18), "hello friend")
        action1(Person("chiclaim", 18), "hello friend")
    }
}
/*
    //-----------------匿名内部类方式实现的
    public final void functionReference() {
      Function2 action0 = (Function2)(new Function2() {
         public Object invoke(Object var1, Object var2) {
            this.invoke((Person)var1, (String)var2);
            return Unit.INSTANCE;
         }

         public final void invoke(@NotNull Person person, @NotNull String message) {
            Intrinsics.checkParameterIsNotNull(person, "person");
            Intrinsics.checkParameterIsNotNull(message, "message");
            MemberReference.this.sendEmail(person, message);
         }
      });
      KFunction action = new Function2((MemberReference)this) {
         public Object invoke(Object var1, Object var2) {
            this.invoke((Person)var1, (String)var2);
            return Unit.INSTANCE;
         }

         public final void invoke(@NotNull Person p1, @NotNull String p2) {
            Intrinsics.checkParameterIsNotNull(p1, "p1");
            Intrinsics.checkParameterIsNotNull(p2, "p2");
            ((MemberReference)this.receiver).sendEmail(p1, p2);
         }

         public final String getName() {
            return "sendEmail";
         }

         public final String getSignature() {
            return "sendEmail(Llambda/Person;Ljava/lang/String;)V";
         }
      };
      action0.invoke(new Person("chiclaim", 18), "hello friend");
      ((Function2)action).invoke(new Person("chiclaim", 18), "hello friend");
   }


 */