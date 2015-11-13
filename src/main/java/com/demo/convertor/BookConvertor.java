/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.demo.convertor;

import com.demo.domain.BookDO;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

/**
 *
 * @author baoxing.gbx
 * @version $Id:BookConvertor.java, V 0.1 2015-11-11 13:36 baoxing.gbx Exp $$
 */
public class BookConvertor {

    public static Document convert2Doc(BookDO bookDO) {
        Document doc = new Document();
        String idstr = bookDO.getId() + "";

        doc.add(new TextField("id", idstr, Field.Store.NO.YES));
        doc.add(new TextField("name", bookDO.getName(), Field.Store.YES));
        doc.add(new TextField("author", bookDO.getAuthor(), Field.Store.YES));
        doc.add(new TextField("content", bookDO.getContent(), Field.Store.YES));
        return doc;
    }
}