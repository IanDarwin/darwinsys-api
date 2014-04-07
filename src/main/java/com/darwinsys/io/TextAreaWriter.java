/* Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 2006.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

// BEGIN main
package com.darwinsys.io;

import java.io.IOException;
import java.io.Writer;

import javax.swing.JTextArea;

/**
 * Simple way to "print" to a JTextArea; just say
 * PrintWriter out = new PrintWriter(new TextAreaWriter(myTextArea));
 * Then out.println() et all will all appear in the TextArea.
 */
public final class TextAreaWriter extends Writer {

    private final JTextArea textArea;

    public TextAreaWriter(final JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void flush(){ }
    
    @Override
    public void close(){ }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        textArea.append(new String(cbuf, off, len));        
    }
}
// END main
