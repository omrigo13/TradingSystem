package spellChecker;

import java.io.*;
import java.net.URL;
import java.util.*;


public class Spelling {

	private final SimpleMap nWords = new SimpleMap();

	public Spelling() {
		try {
			URL url = getClass().getResource("big.txt");
			File f = new File(url.getPath());
			FileReader in = new FileReader(f);
			char[] buffer = new char[(int)f.length()];
			in.read(buffer);
			int begin = 0;
			boolean isUpper = false;
			for (int i = 0; i < buffer.length; i++) {
				while ((('a' > buffer[i] || buffer[i] > 'z') && ('A' > buffer[i] || buffer[i] > 'Z')) && i < buffer.length - 1 ) i++;
				begin = i;
				while ((('a' <= buffer[i] && buffer[i] <= 'z') || (isUpper = ('A' <= buffer[i] && buffer[i] <='Z'))) && i < (buffer.length-1)){
					if(isUpper) buffer[i] = Character.toLowerCase(buffer[i]);
					i++;
				}
				String word  = new String(buffer, begin, i - begin);
				nWords.put(word, (short) (nWords.get(word) + 1));
			}
			in.close();
		}
		catch (IOException e) {
			System.out.println("file not found");
		}
	}

	private final ArrayList<String> edits(String word) {
		ArrayList<String> result = new ArrayList<String>();
		for(int i=0; i < word.length(); ++i) result.add(word.substring(0, i) + word.substring(i+1));
		for(int i=0; i < word.length()-1; ++i) result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
		for(int i=0; i < word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
		for(int i=0; i <= word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
		return result;
	}

	public final String correct(String word) {
		if(word == null)
			return null;
		if(word.trim().equals(""))
			return "";
		if(nWords.containsKey(word)) return word;
		ArrayList<String> list = edits(word);
		IdentityHashMap<Short, String> candidates = new IdentityHashMap<Short, String>();
		for(String s : list) if(nWords.containsKey(s)) candidates.put(nWords.get(s),s);
		if(candidates.size() > 0) return candidates.get(Collections.max(candidates.keySet()));
		for(String s : list) for(String w : edits(s)) if(nWords.containsKey(w)) candidates.put(nWords.get(w),w);
		return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
	}

//	public static void main(String args[]) throws IOException {
//		if(args.length > 0) System.out.println((new Spelling("big.txt")).correct(args[0]));
//	}

}
