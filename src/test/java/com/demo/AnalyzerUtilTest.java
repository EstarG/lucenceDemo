/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.demo;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.lucene.demo.token.*;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.Test;

import junit.framework.TestCase;

/**
 *
 * @author baoxing.gbx
 * @version $Id:AnalyzerUtilTest.java, V 0.1 2015-11-17 15:29 baoxing.gbx Exp $$
 */
public class AnalyzerUtilTest extends TestCase {

    private static Logger logger = Logger.getLogger(AnalyzerUtil.class);

    /**
     * 测试lucene自带分词器   英文支持很好，中文不行
     */
    @Test
    public void testSysAnalyzer() {
        Analyzer a1 = new StandardAnalyzer(Version.LUCENE_35);
        Analyzer a2 = new StopAnalyzer(Version.LUCENE_35);
        Analyzer a3 = new SimpleAnalyzer(Version.LUCENE_35);
        Analyzer a4 = new WhitespaceAnalyzer(Version.LUCENE_35);
        String txt = "this is my house,I am come from yunnang zhaotong,"
                     + "My email is ynkonghao@gmail.com,My QQ is 123456789";

        AnalyzerUtil.displayToken(txt, a1, "StandardAnalyzer");
        AnalyzerUtil.displayToken(txt, a2, "StopAnalyzer");
        AnalyzerUtil.displayToken(txt, a3, "SimpleAnalyzer");
        AnalyzerUtil.displayToken(txt, a4, "WhitespaceAnalyzer");

        AnalyzerUtil.displayAllTokenInfo(txt, a1, "StandardAnalyzer");
        AnalyzerUtil.displayAllTokenInfo(txt, a2, "StopAnalyzer");
        AnalyzerUtil.displayAllTokenInfo(txt, a3, "SimpleAnalyzer");
        AnalyzerUtil.displayAllTokenInfo(txt, a4, "WhitespaceAnalyzer");
    }

    /**
     * 中文分析， 使用MMSegAnalyzer进行中文分词
     */
    @Test
    public void testChineseSysAnalyzer() {
        Analyzer a1 = new StandardAnalyzer(Version.LUCENE_35);
        Analyzer a2 = new StopAnalyzer(Version.LUCENE_35);
        Analyzer a3 = new SimpleAnalyzer(Version.LUCENE_35);
        Analyzer a4 = new WhitespaceAnalyzer(Version.LUCENE_35);

        String dicUrl = AnalyzerUtilTest.class.getClassLoader().getResource("mydata").getPath();
        System.out.println(dicUrl);
        Analyzer a5 = new MMSegAnalyzer(dicUrl);

        String txt = "我是来自中国山东的小伙子";

        AnalyzerUtil.displayToken(txt, a1, "StandardAnalyzer");
        AnalyzerUtil.displayToken(txt, a2, "StopAnalyzer");
        AnalyzerUtil.displayToken(txt, a3, "SimpleAnalyzer");
        AnalyzerUtil.displayToken(txt, a4, "WhitespaceAnalyzer");
        AnalyzerUtil.displayToken(txt, a5, "MMSegAnalyzer");
    }

    /**
     * 测试自定义分词器
     */
    @Test
    public void testMyAnalyzer() {

        Analyzer a = new MyAnalyzer(new SamewordComponentImpl(), new String[]{ "的"});
        String dicUrl = AnalyzerUtilTest.class.getClassLoader().getResource("mydata").getPath();
        System.out.println(dicUrl);
        Analyzer a2 = new MMSegAnalyzer(dicUrl);

        String txt = "我是来自中国山东的小伙子";
        AnalyzerUtil.displayToken(txt, a2, "MMSegAnalyzer");
        AnalyzerUtil.displayToken(txt, a, "MyAnalyzer");

        AnalyzerUtil.displayAllTokenInfo(txt, a2, "MMSegAnalyzer");
        AnalyzerUtil.displayAllTokenInfo(txt, a, "MyAnalyzer");
    }
}