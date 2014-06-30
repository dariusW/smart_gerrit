package pl.agh.smart_gerrit.changes;

import pl.agh.smart_gerrit.changes.model.Diff;
import pl.agh.smart_gerrit.changes.model.DiffType;

public class DiffAnalizator {

	private static final String JAVA_CLASS = "[\\s]*(public )?class [\\w]+.*";
	private static final String JAVA_INNER_CLASS = "[\\s]*(public |private )?(static? )class [\\w]+.*";
	private static final String JAVA_METHOD = "[\\s]*(public |private )?[\\w]+ [\\w]+\\(.*\\).*";
	private static final String JAVA_S_METHOD = "[\\s]*(public |private )?static [\\w]+ [\\w]+\\(.*\\).*";
	private static final String COMMENT = "[\\s]*((\\/\\/[\\w\\s]+$)|(\\/\\*\\*)).*";
	private static final String JUNIT_TEST = "[\\s]*@Test.*";
	

	private static final String SQL_UPDATE = "[\\s]*update .*";
	private static final String SQL_INSERT = "[\\s]*insert .*";
	private static final String SQL_DELETE = "[\\s]*delete .*";
	
	public static CharSequence comment(Diff diff) {
		StringBuilder sb = new StringBuilder();
		boolean skip = false;
		if(diff.getDiffType().equals(DiffType.A) || diff.getDiffType().equals(DiffType.B)){
			for(String s: diff.getContent()){
				if(skip){
					skip=false;
					continue;
				}
				if(s.matches(JAVA_CLASS)){
					sb.append(commentPart("Java class",diff));
				} else if(s.matches(JAVA_INNER_CLASS)){
					sb.append(commentPart("Java inner class",diff));
				} else if(s.matches(JAVA_METHOD)){
					sb.append(commentPart("Java method",diff));
				}else if(s.matches(JAVA_S_METHOD)){
					sb.append(commentPart("Java static method",diff));
				} else if(s.matches(COMMENT)){
					sb.append(commentPart("Comment",diff));
				} else if(s.matches(JUNIT_TEST)){
					sb.append(commentPart("JUnit Test",diff));
					skip=true;
				} else if(s.matches(SQL_UPDATE)){
					sb.append(commentPart("Sql update",diff));
				} else if(s.matches(SQL_INSERT)){
					sb.append(commentPart("Sql insert",diff));
				} else if(s.matches(SQL_DELETE)){
					sb.append(commentPart("Sql delete",diff));
				} 
			}
			
			return sb.toString();
		} 
		
		
		return "Nothing changed";
	}

	private static Object commentPart(String string, Diff diff) {
		return string + (diff.getDiffType().equals(DiffType.A)?" removed. ":" added. ");
	}


}
