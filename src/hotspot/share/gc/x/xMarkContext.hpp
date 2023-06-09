/*
 * Copyright (c) 2021, Oracle and/or its affiliates. All rights reserved.
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

#ifndef SHARE_GC_X_XMARKCONTEXT_HPP
#define SHARE_GC_X_XMARKCONTEXT_HPP

#include "gc/x/xMarkCache.hpp"
#include "gc/shared/stringdedup/stringDedup.hpp"
#include "memory/allocation.hpp"

class XMarkStripe;
class XMarkThreadLocalStacks;

class XMarkContext : public StackObj {
private:
  XMarkCache                    _cache;
  XMarkStripe* const            _stripe;
  XMarkThreadLocalStacks* const _stacks;
  StringDedup::Requests         _string_dedup_requests;

public:
  XMarkContext(size_t nstripes,
               XMarkStripe* stripe,
               XMarkThreadLocalStacks* stacks);

  XMarkCache* cache();
  XMarkStripe* stripe();
  XMarkThreadLocalStacks* stacks();
  StringDedup::Requests* string_dedup_requests();
};

#endif // SHARE_GC_X_XMARKCONTEXT_HPP
