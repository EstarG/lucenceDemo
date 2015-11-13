/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.demo;

import com.demo.domain.BookDO;

import org.junit.Test;

import junit.framework.TestCase;

import java.util.List;

/**
 *
 * @author baoxing.gbx
 * @version $Id:LucenceDemoTest.java, V 0.1 2015-11-11 15:21 baoxing.gbx Exp $$
 */
public class LucenceDemoTest extends TestCase {

    LucenceDemo lucenceDemo = null;

    @Override
    protected void setUp() throws Exception {

        // 数据准备
        lucenceDemo = new LucenceDemo();

        lucenceDemo.init();

        //  循环添加100本书
        for (int i = 1; i <= 2; ++i) {

            BookDO bookDO = new BookDO();
            bookDO.setId(i);
            bookDO.setAuthor("zhangsan" + i);
            bookDO.setName("Java program" + i);
            bookDO.setContent("Java是一种可以撰写跨平台应用程序的面向对象的程序设计开发语言");
            lucenceDemo.addDoc(bookDO);
        }

        for (int i = 3; i <= 4; ++i) {
            BookDO bookDO = new BookDO();
            bookDO.setId(i);
            bookDO.setAuthor("lisi" + i);
            bookDO.setName("Java program" + i);
            bookDO.setContent("Java 技术具有卓越的通用性、高效性、平台移植性和安全性，广泛应用于PC、数据中心、游戏控制台、科学超级计算机、移动电话和互联网");
            lucenceDemo.addDoc(bookDO);
        }

        for (int i = 5; i <= 6; ++i) {
            BookDO bookDO = new BookDO();
            bookDO.setId(i);
            bookDO.setAuthor("wangwu" + i);
            bookDO.setName("Java program" + i);
            bookDO.setContent("同时拥有全球最大的开发者专业社");
            lucenceDemo.addDoc(bookDO);
        }

        for (int i = 7; i <= 8; ++i) {

            BookDO bookDO = new BookDO();
            bookDO.setId(i);
            bookDO.setAuthor("xiaoming" + i);
            bookDO.setName("C++ program" + i);
            bookDO.setContent("C++是在C语言的基础上开发的一种面向对象编程语言");
            lucenceDemo.addDoc(bookDO);
        }
    }

    /**
     * 测试根据书名查找
     */
    @Test
    public void testNameSearch() {

        try {

            lucenceDemo.getIndexWriter().close();

            List<BookDO> bookDOs = lucenceDemo.search("Java");

            System.out.println("查询到" + bookDOs.size() + "本相关书籍。 详细信息如下：");
            for (int i = 0; i < bookDOs.size(); ++i) {
                System.out.println(bookDOs.get(i).toString());
            }

        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    /**
     * 测试根据作者查找
     */
    @Test
    public void testAuthorSearch() {

        try {

            lucenceDemo.getIndexWriter().close();

            List<BookDO> bookDOs = lucenceDemo.search("zhangsan1");

            System.out.println("查询到" + bookDOs.size() + "本相关书籍。 详细信息如下：");
            for (int i = 0; i < bookDOs.size(); ++i) {
                System.out.println(bookDOs.get(i).toString());
            }

        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    /**
     * 测试根据内同查找
     */
    @Test
    public void testContentSearch() {

        try {

            lucenceDemo.getIndexWriter().close();

            List<BookDO> bookDOs = lucenceDemo.search("开发");

            System.out.println("查询到" + bookDOs.size() + "本相关书籍。 详细信息如下：");
            for (int i = 0; i < bookDOs.size(); ++i) {
                System.out.println(bookDOs.get(i).toString());
            }

        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    /**
     * 测试多条件查找
     */
    @Test
    public void testMutiSearch() {

        try {

            lucenceDemo.getIndexWriter().close();

            List<BookDO> bookDOs = lucenceDemo.search("zhangsan1 C++");

            System.out.println("查询到" + bookDOs.size() + "本相关书籍。 详细信息如下：");
            for (int i = 0; i < bookDOs.size(); ++i) {
                System.out.println(bookDOs.get(i).toString());
            }

             bookDOs = lucenceDemo.search("zhangsan1 Java");

            System.out.println("查询到" + bookDOs.size() + "本相关书籍。 详细信息如下：");
            for (int i = 0; i < bookDOs.size(); ++i) {
                System.out.println(bookDOs.get(i).toString());
            }

        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }
}