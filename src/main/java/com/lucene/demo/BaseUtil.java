/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.lucene.demo;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
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
 * @version $Id:BaseUtil.java, V 0.1 2015-11-16 15:32 baoxing.gbx Exp $$
 */
public class BaseUtil {

    protected static final int MAX    = 10;
    protected static Logger    logger = Logger.getLogger(IndexUtil.class);

    /** 索引读 */
    private static IndexReader indexReader;

    /** 索引写 */
    private static IndexWriter indexWriter;

    /** 检索 */
    private static IndexSearcher indexSearcher;

    /** 目录 */
    private static Directory directory;

    /** 模拟数据库 */
    protected static Map<String, Object> dataBase = new HashMap<String, Object>();

    // 初始化
    static {

        try {
            Properties properties = new Properties();
            properties
                .load(IndexUtil.class.getClassLoader().getResourceAsStream("lucence.properties"));

            // 1. 初始化Directory
            directory = FSDirectory.open(new File((String) properties.get("path")));

            // 指定一段代码，会在JVM退出之前执行。
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        if (null != indexWriter) {
                            indexWriter.close();
                        }
                        if (null != indexSearcher) {
                            indexSearcher.close();
                        }

                        if (null != indexReader) {
                            indexReader.close();
                        }
                        if (null != directory) {
                            directory.close();
                        }
                        if (null != dataBase) {
                            dataBase.clear();
                        }

                        logger.info("=== 已经关闭  ===");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        } catch (Exception e) {
            logger.info("初始化错误:" + e.getMessage());
        }
    }

    /**
     * 获取reader
     *
     * @return
     * @throws IOException
     */
    protected static IndexReader getIndexReader() throws IOException {

        if (null != indexReader) {

            IndexReader newIr = IndexReader.openIfChanged(indexReader);
            if (null != newIr) {
                indexReader.close();
                indexReader = newIr;
                indexSearcher = new IndexSearcher(indexReader);
            }
            return indexReader;

        } else {

            return IndexReader.open(directory);
        }
    }

    /**
     * 获取writer
     *
     * @return
     * @throws IOException
     */
    protected static IndexWriter getIndexWriter() throws IOException {

        if (null != indexWriter) {
            return indexWriter;
        } else {
            // 防止并发
            synchronized (IndexUtil.class) {
                //  初始化writer
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35,
                    new StandardAnalyzer(Version.LUCENE_35));
                // 每次都重新创建
                config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
                indexWriter = new IndexWriter(directory, config);
            }
            return indexWriter;
        }
    }

    /**
     * 获取Searcher
     *
     * @return
     * @throws IOException
     */
    protected static IndexSearcher getIndexSearcher() throws IOException {

        if (null != indexSearcher) {
            return indexSearcher;
        } else {
            // 防止并发
            synchronized (IndexUtil.class) {
                indexSearcher = new IndexSearcher(getIndexReader());
            }
            return indexSearcher;
        }
    }

}