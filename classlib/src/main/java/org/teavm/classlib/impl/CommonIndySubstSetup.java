package org.teavm.classlib.impl;



import org.teavm.classlib.impl.lambda.LambdaMetafactorySubstitutor;
import org.teavm.classlib.impl.StringConcatFactorySubstitutor;
import org.teavm.classlib.impl.SwitchBootstrapSubstitutor;
import org.teavm.classlib.impl.record.ObjectMethodsSubstitutor;
import org.teavm.dependency.DependencyAnalyzer;
import org.teavm.model.MethodReference;
import org.teavm.model.ValueType;
import org.teavm.vm.spi.TeaVMHost;

public class CommonIndySubstSetup {
    private CommonIndySubstSetup() {}

    public static void applyTo(TeaVMHost d) {

        LambdaMetafactorySubstitutor lms = new LambdaMetafactorySubstitutor();
        d.add(new MethodReference("java.lang.invoke.LambdaMetafactory", "metafactory",
                ValueType.object("java.lang.invoke.MethodHandles$Lookup"), ValueType.object("java.lang.String"),
                ValueType.object("java.lang.invoke.MethodType"), ValueType.object("java.lang.invoke.MethodType"),
                ValueType.object("java.lang.invoke.MethodHandle"), ValueType.object("java.lang.invoke.MethodType"),
                ValueType.object("java.lang.invoke.CallSite")), lms);
        d.add(new MethodReference("java.lang.invoke.LambdaMetafactory", "altMetafactory",
                ValueType.object("java.lang.invoke.MethodHandles$Lookup"),
                ValueType.object("java.lang.String"), ValueType.object("java.lang.invoke.MethodType"),
                ValueType.arrayOf(ValueType.object("java.lang.Object")),
                ValueType.object("java.lang.invoke.CallSite")), lms);

        d.add(new MethodReference("java.lang.runtime.ObjectMethods", "bootstrap",
                ValueType.object("java.lang.invoke.MethodHandles$Lookup"), ValueType.object("java.lang.String"),
                ValueType.object("java.lang.invoke.TypeDescriptor"), ValueType.object("java.lang.Class"),
                ValueType.object("java.lang.String"),
                ValueType.arrayOf(ValueType.object("java.lang.invoke.MethodHandle")),
                ValueType.object("java.lang.Object")),
                new ObjectMethodsSubstitutor());

        StringConcatFactorySubstitutor stringConcatSubstitutor = new StringConcatFactorySubstitutor();
        d.add(new MethodReference("java.lang.invoke.StringConcatFactory", "makeConcat",
                ValueType.object("java.lang.invoke.MethodHandles$Lookup"), ValueType.object("java.lang.String"),
                ValueType.object("java.lang.invoke.MethodType"), ValueType.object("java.lang.invoke.CallSite")),
                stringConcatSubstitutor);
        d.add(new MethodReference("java.lang.invoke.StringConcatFactory", "makeConcatWithConstants",
                        ValueType.object("java.lang.invoke.MethodHandles$Lookup"), ValueType.object("java.lang.String"),
                        ValueType.object("java.lang.invoke.MethodType"), ValueType.object("java.lang.String"),
                        ValueType.arrayOf(ValueType.object("java.lang.Object")),
                        ValueType.object("java.lang.invoke.CallSite")),
                stringConcatSubstitutor);

        SwitchBootstrapSubstitutor switchBootstrapSubstitutor = new SwitchBootstrapSubstitutor();
        d.add(new MethodReference("java.lang.runtime.SwitchBootstraps", "typeSwitch",
                        ValueType.object("java.lang.invoke.MethodHandles$Lookup"),
                        ValueType.object("java.lang.String"),
                        ValueType.object("java.lang.invoke.MethodType"),
                        ValueType.arrayOf(ValueType.object("java.lang.Object")),
                        ValueType.object("java.lang.invoke.CallSite")),
                switchBootstrapSubstitutor);
        d.add(new MethodReference("java.lang.runtime.SwitchBootstraps", "enumSwitch",
                        ValueType.object("java.lang.invoke.MethodHandles$Lookup"),
                        ValueType.object("java.lang.String"),
                        ValueType.object("java.lang.invoke.MethodType"),
                        ValueType.arrayOf(ValueType.object("java.lang.Object")),
                        ValueType.object("java.lang.invoke.CallSite")),
                switchBootstrapSubstitutor);

    }

}
