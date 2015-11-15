/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.demo;

import com.lucene.demo.IndexUtil;

import org.junit.Test;

import junit.framework.TestCase;

import java.io.IOException;

/**
 *
 * @author baoxing.gbx
 * @version $Id:IndexUtilTest.java, V 0.1 2015-11-15 14:49 baoxing.gbx Exp $$
 */
public class IndexUtilTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        IndexUtil.creatIndex();
    }

    /**
     * 创建索引
     *
     */
    @Test
    public void testCreate() {
        IndexUtil.creatIndex();
    }

    /**
     * 删除索引
     */
    @Test
    public void testDelete() throws IOException {

        IndexUtil.search("Java");
        IndexUtil.numDocs();

        IndexUtil.deleteIndex();

        IndexUtil.search("Java");
        IndexUtil.numDocs();
    }

    /**
     * 删除索引
     */
    @Test
    public void testSearch() throws Exception {

        IndexUtil.search("Java");
        IndexUtil.numDocs();
    }

    @Test
    public void testNuns() throws Exception {

        IndexUtil.numDocs();
    }

    /**
	 * 更新
	 *
	 * @throws Exception
	 */
	@Test
	public static void testUpdate() throws Exception {
		IndexUtil.numDocs();
		IndexUtil.search("Java");

		IndexUtil.updateIndex();

		System.out.println("更新完毕");

		IndexUtil.numDocs();
		IndexUtil.search("Java");
	}

}