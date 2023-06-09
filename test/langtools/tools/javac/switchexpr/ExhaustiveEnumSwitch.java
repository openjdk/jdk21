/*
 * Copyright (c) 2017, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 8206986 8243548
 * @summary Verify that an switch expression over enum can be exhaustive without default.
 * @compile --release 20 ExhaustiveEnumSwitch.java
 * @compile ExhaustiveEnumSwitchExtra.java
 * @run main ExhaustiveEnumSwitch IncompatibleClassChangeError
 * @compile ExhaustiveEnumSwitch.java
 * @compile ExhaustiveEnumSwitchExtra.java
 * @run main ExhaustiveEnumSwitch MatchException
 */

public class ExhaustiveEnumSwitch {
    public static void main(String... args) throws ClassNotFoundException {
        boolean matchException = "MatchException".equals(args[0]);
        new ExhaustiveEnumSwitch().run(matchException);
    }

    private void run(boolean matchException) throws ClassNotFoundException {
        ExhaustiveEnumSwitchEnum v = ExhaustiveEnumSwitchEnum.valueOf("F");

        try {
            print(v);
            throw new AssertionError("Expected exception did not occur.");
        } catch (IncompatibleClassChangeError err) {
            if (matchException) {
                throw new AssertionError("Expected IncompatibleClassChangeError, but got MatchException!");
            }
        } catch (Exception ex) {
            //cannot refer to MatchException directly, as it used to be preview API in JDK 20:
            if (ex.getClass() == Class.forName("java.lang.MatchException")) {
                if (!matchException) {
                    throw new AssertionError("Expected MatchException, but got IncompatibleClassChangeError!");
                }
            } else {
                throw ex;
            }
        }
    }

    private String print(ExhaustiveEnumSwitchEnum t) {
        return switch (t) {
            case A -> "A";
            case B -> "B";
        };
    }

}
enum ExhaustiveEnumSwitchEnum {
    A, B;
    class NestedClass {}
    enum NestedEnum {}
    interface NestedInterface {}
    @interface NestedAnnotation {}
    void nestedMethod() {}
    static void nestedStaticMethod() {}
    int nestedField;
    static int nestedStaticField;
}
