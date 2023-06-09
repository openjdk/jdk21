/*
 * Copyright (c) 2021, Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2021, NTT DATA.
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
 *
 */

package sun.jvm.hotspot.gc.x;

import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.runtime.VMObject;
import sun.jvm.hotspot.types.AddressField;
import sun.jvm.hotspot.types.Type;
import sun.jvm.hotspot.types.TypeDataBase;

public class XRelocate  extends VMObject {

    static {
        VM.registerVMInitializedObserver((o, d) -> initialize(VM.getVM().getTypeDataBase()));
    }

    private static synchronized void initialize(TypeDataBase db) {
        Type type = db.lookupType("XRelocate");
    }

    public XRelocate(Address addr) {
        super(addr);
    }

    private long forwardingIndex(XForwarding forwarding, Address from) {
        long fromOffset = XAddress.offset(from);
        return (fromOffset - forwarding.start()) >>> forwarding.objectAlignmentShift();
    }

    private Address forwardingFind(XForwarding forwarding, Address from) {
        long fromIndex = forwardingIndex(forwarding, from);
        XForwardingEntry entry = forwarding.find(fromIndex);
        return entry.populated() ? XAddress.good(VM.getVM().getDebugger().newAddress(entry.toOffset())) : null;
    }

    public Address forwardObject(XForwarding forwarding, Address from) {
        return forwardingFind(forwarding, from);
    }

    public Address relocateObject(XForwarding forwarding, Address o) {
        Address toAddr = forwardingFind(forwarding, o);
        if (toAddr != null) {
            // Already relocated.
            return toAddr;
        } else {
            // Return original address because it is not yet relocated.
            return o;
        }
    }
}
