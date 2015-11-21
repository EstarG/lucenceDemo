/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.demo;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.demo.convertor.BookConvertor;
import com.demo.domain.BookDO;
import com.lucene.demo.IndexUtil;
import com.lucene.demo.token.AnalyzerUtil;
import com.lucene.demo.token.MyAnalyzer;
import com.lucene.demo.token.SamewordComponentImpl;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

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

        Analyzer a = new MyAnalyzer(new SamewordComponentImpl(), new String[] { "的" });
        String dicUrl = AnalyzerUtilTest.class.getClassLoader().getResource("mydata").getPath();
        System.out.println(dicUrl);
        Analyzer a2 = new MMSegAnalyzer(dicUrl);

        String txt = "我是来自中国山东的小伙子";
        AnalyzerUtil.displayToken(txt, a2, "MMSegAnalyzer");
        AnalyzerUtil.displayToken(txt, a, "MyAnalyzer");

        AnalyzerUtil.displayAllTokenInfo(txt, a2, "MMSegAnalyzer");
        AnalyzerUtil.displayAllTokenInfo(txt, a, "MyAnalyzer");
    }

    /**
     * 测试自定义分词器建立索引并查询
     */
    @Test
    public void test() {

        try {

            Properties properties = new Properties();
            properties
                .load(IndexUtil.class.getClassLoader().getResourceAsStream("lucence.properties"));

            // 1. 初始化Directory
            Directory directory = FSDirectory.open(new File((String) properties.get("path")));

            // 2. 初始化indexWriter  使用自定义分词器
            Analyzer analyzer = new MyAnalyzer(new SamewordComponentImpl(), new String[] { "的" });
//            String dicUrl = AnalyzerUtilTest.class.getClassLoader().getResource("mydata").getPath();
//            Analyzer analyzer = new MMSegAnalyzer(dicUrl);
            IndexWriter indexWriter = new IndexWriter(directory,
                new IndexWriterConfig(Version.LUCENE_35, analyzer)
                    .setOpenMode(IndexWriterConfig.OpenMode.CREATE));

            // 3. 初始化数据
            creatIndex(indexWriter);

            // 4. 高亮显示搜索
            IndexReader indexReader = IndexReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(indexReader);

            // 5. 创建query
            QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_35,
                new String[] { "name", "content" }, analyzer);
            Query query = queryParser.parse("平台");

            TopDocs topDocs = searcher.search(query, 10);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (ScoreDoc scoreDoc : scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                String title = highLighter(analyzer, query, "name", doc);
                String content = highLighter(analyzer, query, "content", doc);
                logger.info("name = " + title + " content = " + content);

            }

            searcher.close();
            indexReader.close();
            indexWriter.close();
            directory.close();

        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        } finally {

        }

    }

    /**
     * 高亮文本
     * @param analyzer
     * @param query
     * @param fieldName
     * @param doc
     * @return
     * @throws IOException
     * @throws InvalidTokenOffsetsException
     */
    private String highLighter(Analyzer analyzer, Query query, String fieldName,
                               Document doc) throws IOException, InvalidTokenOffsetsException {
        // 高亮显示关键字，如果内容中本来就有<span></span>，可能导致显示错乱
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b>",
            "</b>");
        QueryScorer scorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));

        String text = doc.get(fieldName);

        String tmp_text = highlighter.getBestFragment(analyzer, fieldName, text);
        if (null != tmp_text) {
            text = tmp_text;
        } else if (null != text && text.length() > 10){
            text = text.substring(0,10);
        }
        return text;
    }

    /**
     * 创建索引
     */
    public static void creatIndex(IndexWriter indexWriter) throws Exception {

        try {
            //  循环添加书
            for (int i = 1; i <= 2; ++i) {

                BookDO bookDO = new BookDO();
                bookDO.setId(i);
                bookDO.setAuthor("zhangsan" + i);
                bookDO.setName("Java program" + i);
                bookDO.setContent("Java是一种可以撰写跨平台应用程序的面向对象的程序设计开发语言");
                addDoc(bookDO, indexWriter);
            }

            for (int i = 3; i <= 4; ++i) {
                BookDO bookDO = new BookDO();
                bookDO.setId(i);
                bookDO.setAuthor("lisi" + i);
                bookDO.setName("Java program" + i);
                bookDO.setContent(
                    "Java 技术具有卓越的通用性、高效性、平台移植性和安全性，广泛应用于PC、数据中心、游戏控制台、科学超级计算机、移动电话和互联网");
                addDoc(bookDO, indexWriter);
            }

            for (int i = 5; i <= 6; ++i) {
                BookDO bookDO = new BookDO();
                bookDO.setId(i);
                bookDO.setAuthor("wangwu" + i);
                bookDO.setName("Java program" + i);
                bookDO.setContent("同时拥有全球最大的开发者专业社");
                addDoc(bookDO, indexWriter);
            }

            for (int i = 7; i <= 8; ++i) {

                BookDO bookDO = new BookDO();
                bookDO.setId(i);
                bookDO.setAuthor("xiaoming" + i);
                bookDO.setName("C++ program" + i);
                bookDO.setContent("C++是在C语言的基础上开发的一种面向对象编程语言");
                addDoc(bookDO, indexWriter);
            }
        } catch (Exception e) {
            logger.error("索引创建失败:" + e.getMessage());
            throw e;
        }
    }

    /**
     * 模拟数据库添加，同时添加索引
     *
     * @param bookDO
     * @throws IOException
     */
    private static void addDoc(BookDO bookDO, IndexWriter indexWriter) throws IOException {

        // 数据库操作
        // 索引操作
        Document document = BookConvertor.convert2Doc(bookDO);
        indexWriter.addDocument(document);
        // 必须提交否则不奏效
        indexWriter.commit();
    }

}