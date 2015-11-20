/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved/
 */
package com.lucene.demo.token;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author baoxing.gbx
 * @version $Id:SamewordComponentImpl.java, V 0.1 2015-11-17 19:33 baoxing.gbx Exp $$
 */
public class SamewordComponentImpl implements SamewordComponent {

	private Map<String,String[]> maps = new HashMap<String,String[]>();


	public SamewordComponentImpl() {
		maps.put("我是", new String[] {"俺是", "咱是"});
		maps.put("中国", new String[] {"大陆"});
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public String[] getSamewords(String name) {
		return maps.get(name);
	}
}