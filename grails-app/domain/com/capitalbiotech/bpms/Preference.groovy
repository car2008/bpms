package com.capitalbiotech.bpms

class Preference {
	String key
	String value
	
	static String KEY_PREFIX = "KEY_";
    static String KEY_STRING_LIST_SEPARATOR = KEY_PREFIX + "STRING_LIST_SEPARATOR";

	static constraints = {
		key blank: false, unique: true
	}
	
	static mapping = {
		key column: '`preference_key`'
		value column: '`preference_value`', type:'text'
	}
}
