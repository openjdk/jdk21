/*
 * Copyright (c) 2023, Red Hat, Inc. All rights reserved.
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
 * @bug 8303279
 * @summary C2: crash in SubTypeCheckNode::sub() at IGVN split if
 * @run main/othervm -XX:-TieredCompilation -XX:-BackgroundCompilation -XX:+UnlockDiagnosticVMOptions -XX:+StressIGVN -XX:StressSeed=598200189 TestCrashAtIGVNSplitIfSubType
 * @run main/othervm -XX:-TieredCompilation -XX:-BackgroundCompilation -XX:+UnlockDiagnosticVMOptions -XX:+StressIGVN TestCrashAtIGVNSplitIfSubType
 */

public class TestCrashAtIGVNSplitIfSubType {
    private static volatile int barrier;

    public static void main(String[] args) {
        A a = new A();
        B b = new B();
        for (int i = 0; i < 20_000; i++) {
            test(a);
            test(b);
            testHelper1(null, 0);
        }
    }

    private static void test(Object o) {
        int i = 2;
        for (; i < 4; i *= 2) {

        }
        o = testHelper1(o, i);
        if (o instanceof A) {
            barrier = 0x42;
        }
    }

    private static Object testHelper1(Object o, int i) {
        if (i < 3) {
            o = null;
        } else {
            if (o == null) {
            }
        }
        if (i < 2) {
            barrier = 42;
        }
        return o;
    }

    private static class A {
    }

    private static class B {
    }
}
