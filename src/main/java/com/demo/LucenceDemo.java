/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.demo;

import com.demo.convertor.BookConvertor;
import com.demo.domain.BookDO;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * lucence 简单demo
 *
 * @author baoxing.gbx
 * @version $Id:LucenceDemo.java, V 0.1 2015-11-11 14:15 baoxing.gbx Exp $$
 */
public class LucenceDemo {

    /** 索引库存目录 */
    private Directory directory;

    /** 分词器 */
    private Analyzer analyzer;

    /** 索引写 */
    private IndexWriter indexWriter;

    /** 索引查询 */
    private IndexWriterConfig indexWriterConfig;

    /** 索引查询 */
    private IndexSearcher searcher;

    /** 模拟数据库 */
    private static Map<String, Object> dataBase = new HashMap<String, Object>();

    /**
     * 初始化操作
     *
     * @throws IOException
     */
    public void init() throws IOException {

        // 1. 获取lucence索引目录
        Properties properties = new Properties();
        properties
            .load(LucenceDemo.class.getClassLoader().getResourceAsStream("lucence.properties"));
        directory = FSDirectory.open(new File((String) properties.get("path")));
        // 2. 构造分词器
        analyzer = new StandardAnalyzer(Version.LUCENE_40);

        // 3. 构造索引输入
        indexWriterConfig = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        indexWriter = new IndexWriter(directory, indexWriterConfig);

    }

    /**
     * 模拟数据库添加，同时添加索引
     *
     * @param bookDO
     * @throws IOException
     */
    public void addDoc(BookDO bookDO) throws IOException {

        // 数据库操作
        dataBase.put(bookDO.getId() + "", bookDO);
        // 索引操作
        Document document = BookConvertor.convert2Doc(bookDO);
        indexWriter.addDocument(document);
    }

    /**
     * 搜索
     *
     * @param keyword
     * @return
     * @throws Exception
     */
    public List<BookDO> search(String keyword) throws Exception {
        //  构造查询
        IndexReader indexReader = IndexReader.open(directory);
        searcher = new IndexSearcher(indexReader);

        // 支持书名， 作者， 内容搜索
        String[] fields = { "name", "author", "content" };
        QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_40, fields, analyzer);
        queryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
        Query query = queryParser.parse(keyword);

        // 最多给5个
        TopDocs topdocs = searcher.search(query, 5);
        ScoreDoc[] scoreDocs = topdocs.scoreDocs;

        System.out.println("查询结果总数---" + topdocs.totalHits + "最大的评分--" + topdocs.getMaxScore());

        List<BookDO> result = new ArrayList<BookDO>();
        for (int i = 0; i < scoreDocs.length; i++) {
            int doc = scoreDocs[i].doc;
            Document document = searcher.doc(doc);

            System.out.println("docId--" + scoreDocs[i].doc + "---scors--" + scoreDocs[i].score
                               + "---index--" + scoreDocs[i].shardIndex);

            result.add((BookDO) dataBase.get(document.get("id")));
        }
        return result;
    }

    /**
     * Getter method for property <tt>analyzer</tt>.
     *
     * @return property value of analyzer
     */

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * Setter method for property <tt>analyzer</tt>.
     *
     * @param analyzer value to be assigned to property analyzer
     */
    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * Getter method for property <tt>dataBase</tt>.
     *
     * @return property value of dataBase
     */

    public Map<String, Object> getDataBase() {
        return dataBase;
    }

    /**
     * Setter method for property <tt>dataBase</tt>.
     *
     * @param dataBase value to be assigned to property dataBase
     */
    public void setDataBase(Map<String, Object> dataBase) {
        this.dataBase = dataBase;
    }

    /**
     * Getter method for property <tt>directory</tt>.
     *
     * @return property value of directory
     */

    public Directory getDirectory() {
        return directory;
    }

    /**
     * Setter method for property <tt>directory</tt>.
     *
     * @param directory value to be assigned to property directory
     */
    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    /**
     * Getter method for property <tt>indexWriter</tt>.
     *
     * @return property value of indexWriter
     */

    public IndexWriter getIndexWriter() {
        return indexWriter;
    }

    /**
     * Setter method for property <tt>indexWriter</tt>.
     *
     * @param indexWriter value to be assigned to property indexWriter
     */
    public void setIndexWriter(IndexWriter indexWriter) {
        this.indexWriter = indexWriter;
    }

    /**
     * Getter method for property <tt>indexWriterConfig</tt>.
     *
     * @return property value of indexWriterConfig
     */

    public IndexWriterConfig getIndexWriterConfig() {
        return indexWriterConfig;
    }

    /**
     * Setter method for property <tt>indexWriterConfig</tt>.
     *
     * @param indexWriterConfig value to be assigned to property indexWriterConfig
     */
    public void setIndexWriterConfig(IndexWriterConfig indexWriterConfig) {
        this.indexWriterConfig = indexWriterConfig;
    }

    /**
     * Getter method for property <tt>searcher</tt>.
     *
     * @return property value of searcher
     */

    public IndexSearcher getSearcher() {
        return searcher;
    }

    /**
     * Setter method for property <tt>searcher</tt>.
     *
     * @param searcher value to be assigned to property searcher
     */
    public void setSearcher(IndexSearcher searcher) {
        this.searcher = searcher;
    }

}