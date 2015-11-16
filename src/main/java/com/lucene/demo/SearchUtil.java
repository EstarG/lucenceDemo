/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.lucene.demo;

import com.demo.convertor.BookConvertor;
import com.demo.domain.BookDO;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * 查询工具类
 *
 * @author baoxing.gbx
 * @version $Id:SearchUtil.java, V 0.1 2015-11-16 14:09 baoxing.gbx Exp $$
 */
public class SearchUtil extends BaseUtil {

    /**
     * 创建索引
     */
    public static void creatIndex() {

        try {
            //  循环添加书
            for (int i = 1; i <= 5; ++i) {

                BookDO bookDO = new BookDO();
                bookDO.setId(i);
                bookDO.setAuthor("zhangsan" + i);
                bookDO.setName("Java program" + i);
                bookDO.setContent("Java是一种可以撰写跨平台应用程序的面向对象的程序设计开发语言");
                addDoc(bookDO);
            }

            for (int i = 6; i <= 10; ++i) {
                BookDO bookDO = new BookDO();
                bookDO.setId(i);
                bookDO.setAuthor("lisi" + i);
                bookDO.setName("Java program" + i);
                bookDO.setContent(
                    "Java 技术具有卓越的通用性、高效性、平台移植性和安全性，广泛应用于PC、数据中心、游戏控制台、科学超级计算机、移动电话和互联网");
                addDoc(bookDO);
            }

            for (int i = 11; i <= 15; ++i) {
                BookDO bookDO = new BookDO();
                bookDO.setId(i);
                bookDO.setAuthor("wangwu" + i);
                bookDO.setName("Java program" + i);
                bookDO.setContent("同时拥有全球最大的开发者专业社");
                addDoc(bookDO);
            }

            for (int i = 16; i <= 20; ++i) {

                BookDO bookDO = new BookDO();
                bookDO.setId(i);
                bookDO.setAuthor("xiaoming" + i);
                bookDO.setName("C++ program" + i);
                bookDO.setContent("C++是在C语言的基础上开发的一种面向对象编程语言");
                addDoc(bookDO);
            }
        } catch (Exception e) {
            logger.equals("索引创建失败:" + e.getMessage());
        }
    }

    /**
     * 模拟数据库添加，同时添加索引
     *
     * @param bookDO
     * @throws IOException
     */
    private static void addDoc(BookDO bookDO) throws Exception {

        try {

            // 数据库操作
            dataBase.put(bookDO.getId() + "", bookDO);
            // 索引操作
            Document document = BookConvertor.convert2Doc(bookDO);
            IndexWriter indexWriter = getIndexWriter();
            indexWriter.addDocument(document);
            // 必须提交否则不奏效
            indexWriter.commit();

        } catch (Exception e) {
            logger.error("addDoc异常");
            throw e;
        }

    }

    /**
     * 精确查找
     *
     */
    public static void termQuery(String ketword) throws Exception {
        try {

            // 创建search
            IndexSearcher searcher = getIndexSearcher();

            TermQuery query = new TermQuery(new Term("id", ketword));

            // 查询
            TopDocs topDocs = searcher.search(query, MAX);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            logger.info("查询到条数=" + scoreDocs.length);

            for (ScoreDoc scoreDoc : scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                logger.info("doc信息：" + " docId=" + scoreDoc.doc + " id=" + doc.get("id") + " author="
                            + doc.get("author") + " name=" + doc.get("name") + " content="
                            + doc.get("content"));
            }
        } catch (Exception e) {
            logger.error("termQuery查询失败");
            throw e;
        }
    }

    /**
     * 区间检索
     *
     */
    public static void queryByTermRange(String field,String start,String end, boolean includeLStart, boolean includeLEnd) throws Exception {
        try {

            // 创建search
            IndexSearcher searcher = getIndexSearcher();


            TermRangeQuery query = new TermRangeQuery(field, start, end, includeLStart, includeLEnd);

            // 查询
            TopDocs topDocs = searcher.search(query, MAX);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            logger.info("查询到条数=" + scoreDocs.length);

            for (ScoreDoc scoreDoc : scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                logger.info("doc信息：" + " docId=" + scoreDoc.doc + " id=" + doc.get("id") + " author="
                        + doc.get("author") + " name=" + doc.get("name") + " content="
                        + doc.get("content"));
            }
        } catch (Exception e) {
            logger.error("termQuery查询失败");
            throw e;
        }
    }

    /**
     * 模糊搜索
     *
     * @param field
     * @param value
     * @param minimumSimilarity
     * @throws Exception
     */
    public static void fuzzyQuery(String field, String value, float minimumSimilarity) throws Exception {
        try {


            // 创建search
            IndexSearcher searcher = getIndexSearcher();


            FuzzyQuery query = new FuzzyQuery(new Term(field, value), minimumSimilarity);
            // 查询
            TopDocs topDocs = searcher.search(query, MAX);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            logger.info("查询到条数=" + scoreDocs.length);

            for (ScoreDoc scoreDoc : scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                logger.info("doc信息：" + " docId=" + scoreDoc.doc + " id=" + doc.get("id") + " author="
                        + doc.get("author") + " name=" + doc.get("name") + " content="
                        + doc.get("content"));
            }
        } catch (Exception e) {
            logger.error("termQuery查询失败");
            throw e;
        }
    }

    /**
     * 多条搜索
     * @param fields
     * @param value
     * @throws Exception
     */
    public static void multiFieldQuery( String[] fields, String value) throws Exception {
        try {


            // 创建search
            IndexSearcher searcher = getIndexSearcher();


            QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_35, fields,
                    new StandardAnalyzer(Version.LUCENE_35));

            queryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
            Query query = queryParser.parse(value);

            // 查询
            TopDocs topDocs = searcher.search(query, 100);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            logger.info("查询到条数=" + scoreDocs.length);

            for (ScoreDoc scoreDoc : scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                logger.info("doc信息：" + " docId=" + scoreDoc.doc + " id=" + doc.get("id") + " author="
                        + doc.get("author") + " name=" + doc.get("name") + " content="
                        + doc.get("content"));
            }
        } catch (Exception e) {
            logger.error("termQuery查询失败");
            throw e;
        }
    }



    /**
     * 多条搜索
     * @param fields
     * @param value
     * @throws Exception
     */
    public static void queryParser(String field, String value) throws Exception {
        try {


            // 创建search
            IndexSearcher searcher = getIndexSearcher();


            QueryParser queryParser = new QueryParser(Version.LUCENE_35, field, new StandardAnalyzer(Version.LUCENE_35));

            queryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
            Query query = queryParser.parse(value);

            // 查询
            TopDocs topDocs = searcher.search(query, 100);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            logger.info("查询到条数=" + scoreDocs.length);

            for (ScoreDoc scoreDoc : scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                logger.info("doc信息：" + " docId=" + scoreDoc.doc + " id=" + doc.get("id") + " author="
                        + doc.get("author") + " name=" + doc.get("name") + " content="
                        + doc.get("content"));
            }
        } catch (Exception e) {
            logger.error("termQuery查询失败");
            throw e;
        }
    }

}