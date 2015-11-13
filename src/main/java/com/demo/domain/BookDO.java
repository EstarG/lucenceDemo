/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.demo.domain;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * @author baoxing.gbx
 * @version $Id:BookDO.java, V 0.1 2015-11-09 22:30 baoxing.gbx Exp $$
 */
public class BookDO {

	/** 主键 */
    private int    id;

	/** 书名 */
    private String name;

	/** 作者 */
	private String author;

	/** 内容 */
    private String content;

    /**
     * Getter method for property <tt>author</tt>.
     *
     * @return property value of author
     */

    public String getAuthor() {
        return author;
    }

    /**
     * Setter method for property <tt>author</tt>.
     *
     * @param author value to be assigned to property author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Getter method for property <tt>content</tt>.
     *
     * @return property value of content
     */

    public String getContent() {
        return content;
    }

    /**
     * Setter method for property <tt>content</tt>.
     *
     * @param content value to be assigned to property content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Getter method for property <tt>id</tt>.
     *
     * @return property value of id
     */

    public int getId() {
        return id;
    }

    /**
     * Setter method for property <tt>id</tt>.
     *
     * @param id value to be assigned to property id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method for property <tt>name</tt>.
     *
     * @return property value of name
     */

    public String getName() {
        return name;
    }

    /**
     * Setter method for property <tt>name</tt>.
     *
     * @param name value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}