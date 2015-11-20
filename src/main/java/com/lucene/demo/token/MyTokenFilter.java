/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.lucene.demo.token;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;
import java.util.Stack;

/**
 *
 * @author baoxing.gbx
 * @version $Id:MyTokenFilter.java, V 0.1 2015-11-17 19:39 baoxing.gbx Exp $$
 */
public class MyTokenFilter extends TokenFilter {

    /** 词汇单元 */
    private CharTermAttribute cta;

    /** 词汇单元位置增量 */
    private PositionIncrementAttribute pia;

    /** 词汇单元的偏移量 */
    private OffsetAttribute oa;

	/** 同义词 */
    private Stack<String> sames = null;

	/** 同义词组件 */
    private SamewordComponent samewordComponent;

    private AttributeSource.State current;

    public MyTokenFilter(TokenStream input, SamewordComponent samewordComponent) {
        super(input);
        cta = this.addAttribute(CharTermAttribute.class);
        pia = this.addAttribute(PositionIncrementAttribute.class);
        oa = this.addAttribute(OffsetAttribute.class);
        this.samewordComponent = samewordComponent;
        sames = new Stack<String>();
    }

	/**
	 *
	 * @return
	 * @throws IOException
	 */
    @Override
    public final boolean incrementToken() throws IOException {

        if (sames.size() > 0) {

			String str = sames.pop();
            restoreState(current);
            cta.setEmpty();
            cta.append(str);
            pia.setPositionIncrement(0);

            return true;
        }

        if (!this.input.incrementToken()) {
            return false;
        }

        if (addSames(cta.toString())) {
            current = captureState();
        }
        return true;
    }

	/**
	 *
	 * @param name
	 * @return
	 */
    private boolean addSames(String name) {
        String[] sws = samewordComponent.getSamewords(name);
        if (sws != null) {
            for (String str : sws) {
                sames.push(str);
            }
            return true;
        }
        return false;
    }

}