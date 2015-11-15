/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.demo.convertor;

import com.demo.domain.BookDO;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 *
 * @author baoxing.gbx
 * @version $Id:BookConvertor.java, V 0.1 2015-11-11 13:36 baoxing.gbx Exp $$
 */
public class BookConvertor {

    public static Document convert2Doc(BookDO bookDO) {
        Document doc = new Document();

        doc.add(new Field("id", bookDO.getId() + "", Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("name", bookDO.getName(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("author", bookDO.getAuthor(),Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("content", bookDO.getContent(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
        return doc;
    }
}