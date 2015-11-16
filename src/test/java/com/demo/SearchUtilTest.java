/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.demo;

import com.lucene.demo.SearchUtil;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * 更多例子 请看： http://www.cnblogs.com/E-star/p/4969458.html
 *
 * @author baoxing.gbx
 * @version $Id:SearchUtilTest.java, V 0.1 2015-11-16 14:37 baoxing.gbx Exp $$
 */
public class SearchUtilTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        SearchUtil.creatIndex();
    }

    /**
     * 测试精确查找
     */
    @Test
    public void testTermqQuery() {

        try {

            SearchUtil.termQuery("java");

        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    /**
     * 测试区间查找
     */
    @Test
    public void testTermqRangeQuery() {

        try {

            SearchUtil.queryByTermRange("author", "a", "lisi7", true, true);

        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    /**
     * 测试模糊查找
     */
    @Test
    public void testfuzzyQuery() {

        try {

            SearchUtil.fuzzyQuery("name", "progra", 0.1f);

        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }


	/**
	 * 测试多条件查询
	 */
	@Test
	public void testMultiFieldQuery() {

		try {
			String[] fields = { "name", "author", "content" };
			SearchUtil.multiFieldQuery(fields, "java");

		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
		}
	}

	/**
	 * 测试多条件查询
	 */
	@Test
	public void testQueryParser() {

		try {

			SearchUtil.queryParser("name", "Java AND program6");

			SearchUtil.queryParser("name", "Java AND author:lisi6");

		} catch (Exception e) {
			assertTrue(e.getMessage(), false);
		}
	}


}