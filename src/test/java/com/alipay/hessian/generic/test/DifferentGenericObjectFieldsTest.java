package com.alipay.hessian.generic.test;

import com.alipay.hessian.generic.io.GenericSerializerFactory;
import com.alipay.hessian.generic.model.GenericObject;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DifferentGenericObjectFieldsTest {

    @Test
    public void testDifferentGenericObjectFields() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
        hessian2Output.setSerializerFactory(new GenericSerializerFactory());
        GenericObject genericPerson1 = new GenericObject("com.alipay.hessian.generic.test.Person");
        genericPerson1.putField("name", "wang");
        genericPerson1.putField("age", 14);

        GenericObject genericPerson2 = new GenericObject("com.alipay.hessian.generic.test.Person");
        genericPerson2.putField("name", "zhang");

        GenericObject genericPerson3 = new GenericObject("com.alipay.hessian.generic.test.Person");
        genericPerson3.putField("name", "zhao");
        genericPerson3.putField("age", 16);
        hessian2Output.writeObject(genericPerson1);
        hessian2Output.writeObject(genericPerson2);
        hessian2Output.writeObject(genericPerson3);
        hessian2Output.close();
        byte[] serialized = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serialized);
        Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);
        hessian2Input.setSerializerFactory(new SerializerFactory());
        Person p1 = (Person)hessian2Input.readObject();
        assert p1.getName().equals("wang");
        assert p1.getAge() == 14;
        Person p2 = (Person)hessian2Input.readObject();
        assert p2.getName().equals("zhang");
        Person p3 = (Person)hessian2Input.readObject();
        assert p3.getName().equals("zhao");
        assert p3.getAge() == 16;
        hessian2Input.close();

    }
}
