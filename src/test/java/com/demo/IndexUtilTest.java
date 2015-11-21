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
        //IndexUtil.creatIndex();
    }

    /**
     * 创建索引
     *
     */
    @Test
    public void testCreate() {

        try {
            IndexUtil.creatIndex();
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    /**
     * 删除索引
     */
    @Test
    public void testDelete() {

        try {
            IndexUtil.search("Java");
            IndexUtil.numDocs();

            IndexUtil.deleteIndex();

            IndexUtil.search("Java");
            IndexUtil.numDocs();
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    /**
     * 删除索引
     */
    @Test
    public void testDeleteAll() {

        try {

            IndexUtil.search("Java");
            IndexUtil.numDocs();

            IndexUtil.deleteAllIndex();

            IndexUtil.search("Java");
            IndexUtil.numDocs();
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }

    }

    /**
     * 删除索引
     */
    @Test
    public void testSearch() throws Exception {

        try {

            IndexUtil.search("Java");
            IndexUtil.numDocs();

        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }

    }

    @Test
    public void testNuns() throws Exception {

        try {

            IndexUtil.numDocs();

        } catch (IOException e) {
            assertTrue(e.getMessage(), false);
        }
    }

    /**
     * 更新
     *
     * @throws Exception
     */
    @Test
    public static void testUpdate() throws Exception {

        try {

            IndexUtil.numDocs();
            IndexUtil.search("Java");

            IndexUtil.updateIndex();

            System.out.println("更新完毕");

            IndexUtil.numDocs();
            IndexUtil.search("Java");

        } catch (IOException e) {
            assertTrue(e.getMessage(), false);
        }
    }
}