/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.lucene.demo;

import com.demo.convertor.BookConvertor;
import com.demo.domain.BookDO;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author baoxing.gbx
 * @version $Id:IndexUtil.java, V 0.1 2015-11-15 14:23 baoxing.gbx Exp $$
 */
public class IndexUtil {

    private static final int MAX    = 10;
    private static Logger    logger = Logger.getLogger(IndexUtil.class);

    /** 索引读 */
    private static IndexReader indexReader;

    /** 索引写 */
    private static IndexWriter indexWriter;

    /** 目录 */
    private static Directory directory;

    /** 模拟数据库 */
    private static Map<String, Object> dataBase = new HashMap<String, Object>();

    // 初始化
    static {

        try {
            Properties properties = new Properties();
            properties
                .load(IndexUtil.class.getClassLoader().getResourceAsStream("lucence.properties"));

            // 1. 初始化Directory
            directory = FSDirectory.open(new File((String) properties.get("path")));

            // 2. 初始化writer
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35,
                new StandardAnalyzer(Version.LUCENE_35));
			// 每次都重新创建
			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(directory, config);

            // 3. 初始化reader
            indexReader = IndexReader.open(directory);

        } catch (Exception e) {
            logger.info("初始化错误:" + e.getMessage());
        }
    }

    /**
     * 创建索引
     */
    public static void creatIndex() {

        try {
            //  循环添加书
            for (int i = 1; i <= 2; ++i) {

                BookDO bookDO = new BookDO();
                bookDO.setId(i);
                bookDO.setAuthor("zhangsan" + i);
                bookDO.setName("Java program" + i);
                bookDO.setContent("Java是一种可以撰写跨平台应用程序的面向对象的程序设计开发语言");
                addDoc(bookDO);
            }

            for (int i = 3; i <= 4; ++i) {
                BookDO bookDO = new BookDO();
                bookDO.setId(i);
                bookDO.setAuthor("lisi" + i);
                bookDO.setName("Java program" + i);
                bookDO.setContent(
                    "Java 技术具有卓越的通用性、高效性、平台移植性和安全性，广泛应用于PC、数据中心、游戏控制台、科学超级计算机、移动电话和互联网");
                addDoc(bookDO);
            }

            for (int i = 5; i <= 6; ++i) {
                BookDO bookDO = new BookDO();
                bookDO.setId(i);
                bookDO.setAuthor("wangwu" + i);
                bookDO.setName("Java program" + i);
                bookDO.setContent("同时拥有全球最大的开发者专业社");
                addDoc(bookDO);
            }

            for (int i = 7; i <= 8; ++i) {

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
     * 删除索引
     */
    public static void deleteIndex() {
        Term term = new Term("id", 1 + "");
        try {
            indexWriter.deleteDocuments(term);
            indexWriter.commit();
        } catch (IOException e) {
            logger.equals("索引删除失败:" + e.getMessage());
        }
    }

    /**
     * 删除索引
     */
    public static void updateIndex() {
        Term term = new Term("id", 1 + "");
        BookDO bookDO = new BookDO();
        bookDO.setId(1);
        bookDO.setAuthor("zhangsan" + 1);
        bookDO.setName("Java program" + 1);
        bookDO.setContent("Java");

        try {
            indexWriter.updateDocument(term, BookConvertor.convert2Doc(bookDO));
            indexWriter.commit();
        } catch (IOException e) {
            logger.equals("索引更新失败:" + e.getMessage());
        }
    }


    /**
     * 模拟数据库添加，同时添加索引
     *
     * @param bookDO
     * @throws IOException
     */
    private static void addDoc(BookDO bookDO) throws IOException {

        // 数据库操作
        dataBase.put(bookDO.getId() + "", bookDO);
        // 索引操作
        Document document = BookConvertor.convert2Doc(bookDO);
        indexWriter.addDocument(document);
        // 必须提交否则不奏效
        indexWriter.commit();
    }

	/**
	 * 检索
	 *
	 * @param keyword
	 */
    public static void search(String keyword) {
        try {
            indexReader = getIndexReader();

            // 创建search
            IndexSearcher searcher = new IndexSearcher(indexReader);

            String[] fields = { "name", "author", "content" };
            QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_35, fields,
                new StandardAnalyzer(Version.LUCENE_35));
            queryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
            Query query = queryParser.parse(keyword);

            // 查询
            TopDocs topDocs = searcher.search(query, MAX);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            logger.info("查询到条数=" + scoreDocs.length);

            for (ScoreDoc scoreDoc : scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                logger.info("doc信息：" + "docId=" + scoreDoc.doc + "id=" + doc.get("id") + "author="
                            + doc.get("author") + "name=" + doc.get("name") + "content="
                            + doc.get("content"));
            }

        } catch (Exception e) {
            logger.equals("查询失败:" + e.getMessage());
        } finally {
		}
	}

    private static IndexReader getIndexReader() throws IOException {
        // 清新获取reader， 保证seacher最新的索引
        IndexReader newIr = IndexReader.openIfChanged(indexReader);
        if (null != newIr) {
			indexReader.close();
			indexReader = newIr;
		}
        return indexReader;
    }

    public static void numDocs() throws IOException {
        indexReader = getIndexReader();
		logger.info("已删除的数量" + indexReader.numDeletedDocs());
		logger.info("numDocs" + indexReader.numDocs());
		logger.info("maxDoc" + indexReader.maxDoc());
	}




}