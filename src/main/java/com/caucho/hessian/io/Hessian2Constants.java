/*
 * Copyright (c) 2001-2006 Caucho Technology, Inc.  All rights reserved.
 *
 * The Apache Software License, Version 1.1
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Caucho Technology (http://www.caucho.com/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Burlap", "Resin", and "Caucho" must not be used to
 *    endorse or promote products derived from this software without prior
 *    written permission. For written permission, please contact
 *    info@caucho.com.
 *
 * 5. Products derived from this software may not be called "Resin"
 *    nor may "Resin" appear in their names without prior written
 *    permission of Caucho Technology.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL CAUCHO TECHNOLOGY OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Scott Ferguson
 */

package com.caucho.hessian.io;

public interface Hessian2Constants {
    int  INT_DIRECT_MIN    = -0x10;
    int  INT_DIRECT_MAX    = 0x2f;
    int  INT_ZERO          = 0x90;

    int  INT_BYTE_MIN      = -0x800;
    int  INT_BYTE_MAX      = 0x7ff;
    int  INT_BYTE_ZERO     = 0xc8;

    int  INT_SHORT_MIN     = -0x40000;
    int  INT_SHORT_MAX     = 0x3ffff;
    int  INT_SHORT_ZERO    = 0xd4;

    long LONG_DIRECT_MIN   = -0x08;
    long LONG_DIRECT_MAX   = 0x0f;
    int  LONG_ZERO         = 0xe0;

    long LONG_BYTE_MIN     = -0x800;
    long LONG_BYTE_MAX     = 0x7ff;
    int  LONG_BYTE_ZERO    = 0xf8;

    int  LONG_SHORT_MIN    = -0x40000;
    int  LONG_SHORT_MAX    = 0x3ffff;
    int  LONG_SHORT_ZERO   = 0x3c;

    int  STRING_DIRECT_MAX = 0x1f;
    int  STRING_DIRECT     = 0x00;

    int  BYTES_DIRECT_MAX  = 0x0f;
    int  BYTES_DIRECT      = 0x20;
    // 0x30-0x37 is reserved

    int  LONG_INT          = 0x77;

    int  DOUBLE_ZERO       = 0x67;
    int  DOUBLE_ONE        = 0x68;
    int  DOUBLE_BYTE       = 0x69;
    int  DOUBLE_SHORT      = 0x6a;
    int  DOUBLE_FLOAT      = 0x6b;

    int  LENGTH_BYTE       = 0x6e;
    int  LIST_FIXED        = 0x76;    // 'v'

    int  REF_BYTE          = 0x4a;
    int  REF_SHORT         = 0x4b;

    int  TYPE_REF          = 0x75;
}
