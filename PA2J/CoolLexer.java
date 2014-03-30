/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	return filename;
    }
    private void string_buf_append(String str) {
        if (string_buf.length() < MAX_STR_CONST) {
            string_buf.append(str);
        }
        /* check length AFTER appending */
        if (string_buf.length() >= MAX_STR_CONST) {
            yybegin(STRING_ERROR);
        }
    }
    private void string_buf_append(char c) {
        string_buf_append(String.valueOf(c));
    } 
    private int comment_level = 0;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 3;
	private final int STRING_ERROR = 4;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int INLINECOMMENT = 2;
	private final int yy_state_dtrans[] = {
		0,
		71,
		94,
		55,
		99
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NOT_ACCEPT,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NOT_ACCEPT,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NO_ANCHOR,
		/* 181 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"58,62:7,63:2,61,64,63,57,62:18,63,62,56,62:5,16,17,11,13,3,9,4,12,37:10,6,5" +
",8,1,2,62,7,38,39,40,41,42,24,39,43,44,39:2,45,39,46,47,48,39,49,50,29,51,5" +
"2,53,39:3,62,59,62:2,55,62,20,60,18,32,22,23,54,27,25,54:2,19,54,26,31,33,5" +
"4,28,21,35,36,30,34,54:3,14,62,15,10,62,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,182,
"0,1,2,1:6,3,4,1,5,1:4,6,1,7,8,9,1,10,1:7,11:2,12,11:8,13,11:7,1,14,1:3,15,1" +
":15,16,17,18,19,13:2,20,13:8,11,13:5,21,22,23,24,25,26,27,28,29,30,31,32,33" +
",34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58" +
",59,60,61,62,63,64,11,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82" +
",83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,11,13,99,100,101,102,103,1" +
"04,105,106,107")[0];

	private int yy_nxt[][] = unpackFromString(108,65,
"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,130,171:2,173,73,20,96,132," +
"171:2,74,171,100,171,175,177,179,171,21,172:2,174,172,176,172,97,131,133,10" +
"1,178,172:4,180,171,3,22,23,3:2,171,24,3,23:2,-1:67,25,-1:63,26,-1:7,27,-1:" +
"64,28,-1:72,29,-1:58,30,-1:71,171,181,134,171:16,136,134,171:6,181,171:9,13" +
"6,-1:4,171,-1:22,172:7,75,172:18,75,172:11,-1:4,172,-1:41,21,-1:84,23,-1:5," +
"23:2,-1:18,171:19,136,171:17,136,-1:4,171,-1:22,171:9,162,171:9,136,171:5,1" +
"62,171:11,136,-1:4,171,-1:22,172:38,-1:4,172,-1:61,51,-1:6,51,1,93:55,56,93" +
",57,72,93,58,93:3,1,50:10,92,50:4,98,50:40,51,50:3,24,50:2,51,-1,59:22,60,5" +
"9:2,61,59:8,62,59:21,-1,63,59,64,65,59:3,-1:18,171:2,144,171:4,31,171:11,13" +
"6,144,171:5,31,171:10,136,-1:4,171,-1:22,172:9,135,172:15,135,172:12,-1:4,1" +
"72,-1:22,172:9,157,172:15,157,172:12,-1:4,172,-1:21,52,-1:48,93:55,-1,93,-1" +
":2,93,-1,93:3,1,50:56,51,50:3,54,50:2,51,-1,69:56,-1,69:3,70,69:3,-1:18,171" +
":3,146,171,32:2,171,33,171:10,136,171:8,33,171:3,146,171:4,136,-1:4,171,-1:" +
"22,172:3,145,172,76:2,172,77,172:19,77,172:3,145,172:5,-1:4,172,-1:15,53,-1" +
":53,1,66:55,67,66:2,95,66,68,66:3,-1:18,171:5,34:2,171:12,136,171:17,136,-1" +
":4,171,-1:22,172:5,78:2,172:31,-1:4,172,-1:22,171:11,35,171:5,35,171,136,17" +
"1:17,136,-1:4,171,-1:22,172:11,79,172:5,79,172:20,-1:4,172,-1:22,171:16,36," +
"171:2,136,171:15,36,171,136,-1:4,171,-1:22,172:16,80,172:18,80,172:2,-1:4,1" +
"72,-1:22,171:11,37,171:5,37,171,136,171:17,136,-1:4,171,-1:22,172:11,81,172" +
":5,81,172:20,-1:4,172,-1:22,171:4,38,171:14,136,171:4,38,171:12,136,-1:4,17" +
"1,-1:22,172:8,42,172:19,42,172:9,-1:4,172,-1:22,171:15,39,171:3,136,171:10," +
"39,171:6,136,-1:4,171,-1:22,172:4,82,172:19,82,172:13,-1:4,172,-1:22,171:4," +
"40,171:14,136,171:4,40,171:12,136,-1:4,171,-1:22,172:4,84,172:19,84,172:13," +
"-1:4,172,-1:22,41,171:18,136,171:2,41,171:14,136,-1:4,171,-1:22,85,172:21,8" +
"5,172:15,-1:4,172,-1:22,171,43,171:17,136,171:7,43,171:9,136,-1:4,171,-1:22" +
",172:15,83,172:14,83,172:7,-1:4,172,-1:22,171:8,86,171:10,136,171:8,86,171:" +
"8,136,-1:4,171,-1:22,172,87,172:25,87,172:10,-1:4,172,-1:22,171:4,44,171:14" +
",136,171:4,44,171:12,136,-1:4,171,-1:22,172:3,88,172:28,88,172:5,-1:4,172,-" +
"1:22,171:3,45,171:15,136,171:12,45,171:4,136,-1:4,171,-1:22,172:4,89,172:19" +
",89,172:13,-1:4,172,-1:22,171:4,46,171:14,136,171:4,46,171:12,136,-1:4,171," +
"-1:22,172:14,90,172:8,90,172:14,-1:4,172,-1:22,171:4,47,171:14,136,171:4,47" +
",171:12,136,-1:4,171,-1:22,172:3,91,172:28,91,172:5,-1:4,172,-1:22,171:14,4" +
"8,171:4,136,171:3,48,171:13,136,-1:4,171,-1:22,171:3,49,171:15,136,171:12,4" +
"9,171:4,136,-1:4,171,-1:22,171:4,102,171:8,138,171:5,136,171:4,102,171:4,13" +
"8,171:7,136,-1:4,171,-1:22,172:4,103,172:8,147,172:10,103,172:4,147,172:8,-" +
"1:4,172,-1:22,171:4,104,171:8,106,171:5,136,171:4,104,171:4,106,171:7,136,-" +
"1:4,171,-1:22,172:4,105,172:8,107,172:10,105,172:4,107,172:8,-1:4,172,-1:22" +
",171:3,108,171:15,136,171:12,108,171:4,136,-1:4,171,-1:22,172:4,109,172:19," +
"109,172:13,-1:4,172,-1:22,172:2,153,172:17,153,172:17,-1:4,172,-1:22,171:13" +
",110,171:5,136,171:9,110,171:7,136,-1:4,171,-1:22,172:3,111,172:28,111,172:" +
"5,-1:4,172,-1:22,171:3,112,171:15,136,171:12,112,171:4,136,-1:4,171,-1:22,1" +
"72:3,113,172:28,113,172:5,-1:4,172,-1:22,171:2,114,171:16,136,114,171:16,13" +
"6,-1:4,171,-1:22,172:2,115,172:17,115,172:17,-1:4,172,-1:22,171,158,171:17," +
"136,171:7,158,171:9,136,-1:4,171,-1:22,172:12,155,172:21,155,172:3,-1:4,172" +
",-1:22,171:12,160,171:6,136,171:14,160,171:2,136,-1:4,171,-1:22,172:13,117," +
"172:15,117,172:8,-1:4,172,-1:22,171:13,116,171:5,136,171:9,116,171:7,136,-1" +
":4,171,-1:22,172:13,119,172:15,119,172:8,-1:4,172,-1:22,171:7,164,171:11,13" +
"6,171:6,164,171:10,136,-1:4,171,-1:22,172:7,159,172:18,159,172:11,-1:4,172," +
"-1:22,171:4,118,171:14,136,171:4,118,171:12,136,-1:4,171,-1:22,172:3,121,17" +
"2:28,121,172:5,-1:4,172,-1:22,171:18,120,136,171:13,120,171:3,136,-1:4,171," +
"-1:22,172:13,161,172:15,161,172:8,-1:4,172,-1:22,171:3,122,171:15,136,171:1" +
"2,122,171:4,136,-1:4,171,-1:22,172:4,163,172:19,163,172:13,-1:4,172,-1:22,1" +
"71:3,124,171:15,136,171:12,124,171:4,136,-1:4,171,-1:22,172,123,172:25,123," +
"172:10,-1:4,172,-1:22,171:13,166,171:5,136,171:9,166,171:7,136,-1:4,171,-1:" +
"22,172:7,125,172:18,125,172:11,-1:4,172,-1:22,171:4,168,171:14,136,171:4,16" +
"8,171:12,136,-1:4,171,-1:22,172:10,165,172:20,165,172:6,-1:4,172,-1:22,171," +
"126,171:17,136,171:7,126,171:9,136,-1:4,171,-1:22,172:7,167,172:18,167,172:" +
"11,-1:4,172,-1:22,171:7,128,171:11,136,171:6,128,171:10,136,-1:4,171,-1:22," +
"172:11,127,172:5,127,172:20,-1:4,172,-1:22,171:10,169,171:8,136,171:11,169," +
"171:5,136,-1:4,171,-1:22,171:7,170,171:11,136,171:6,170,171:10,136,-1:4,171" +
",-1:22,171:11,129,171:5,129,171,136,171:17,136,-1:4,171,-1:22,171,140,171,1" +
"42,171:15,136,171:7,140,171:4,142,171:4,136,-1:4,171,-1:22,172,137,139,172:" +
"17,139,172:6,137,172:10,-1:4,172,-1:22,171:13,148,171:5,136,171:9,148,171:7" +
",136,-1:4,171,-1:22,172,141,172,143,172:23,141,172:4,143,172:5,-1:4,172,-1:" +
"22,171:9,150,171:9,136,171:5,150,171:11,136,-1:4,171,-1:22,172:13,149,172:1" +
"5,149,172:8,-1:4,172,-1:22,171:9,152,154,171:8,136,171:5,152,171:5,154,171:" +
"5,136,-1:4,171,-1:22,172:9,151,172:15,151,172:12,-1:4,172,-1:22,171:2,156,1" +
"71:16,136,156,171:16,136,-1:4,171,-1:4");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
    case YYINITIAL:
    /* it's ok to reach EOF in normal state */
    case INLINECOMMENT: 
    /* it's ok to reach EOF in inlinecomment */
	    return new Symbol(TokenConstants.EOF);
    case COMMENT:
    /* it's an error to reach EOF in comment */
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    case STRING:
    /* it's an error to reach EOF in string */
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in String constant");
    }
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(TokenConstants.EQ); }
					case -3:
						break;
					case 3:
						{ /* This rule should be the very last
                                 in your lexical specification and
                                 will match match everything not
                                 matched by other lexical rules. */
                              return new Symbol(TokenConstants.ERROR, yytext()); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.COMMA); }
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.DOT); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.SEMI); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.COLON); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.AT); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.LT); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.MINUS); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.NEG); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.MULT); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.DIV); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.PLUS); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -19:
						break;
					case 19:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -20:
						break;
					case 20:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -21:
						break;
					case 21:
						{ 
    String intString = yytext();
    AbstractSymbol entry = AbstractTable.inttable.addString(intString);                                            
    return new Symbol(TokenConstants.INT_CONST, entry);
}
					case -22:
						break;
					case 22:
						{ 
    yybegin(STRING);
    string_buf.setLength(0); /* clear string buffer */
}
					case -23:
						break;
					case 23:
						{}
					case -24:
						break;
					case 24:
						{ curr_lineno++; }
					case -25:
						break;
					case 25:
						{ /* Sample lexical rule for "=>" arrow.
                                                 Further lexical rules should be defined
                                                 here, after the last %% separator */
                                              return new Symbol(TokenConstants.DARROW); }
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.LE); }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -28:
						break;
					case 28:
						{ yybegin(INLINECOMMENT); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }
					case -30:
						break;
					case 30:
						{ yybegin(COMMENT); comment_level++; }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.FI); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.IF); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.IN); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.OF); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.LET); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.NEW); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.NOT); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.CASE); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.LOOP); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ELSE); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.ESAC); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.THEN); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.POOL); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.BOOL_CONST, java.lang.Boolean.TRUE); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.CLASS); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.BOOL_CONST, java.lang.Boolean.FALSE); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.WHILE); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -50:
						break;
					case 50:
						{}
					case -51:
						break;
					case 51:
						{/* JLex dot metacharactor doesn't match newline 
                                        and carriage return. JLex doesn't know vertical
                                        tab \v so use numeric \x0b insted */}
					case -52:
						break;
					case 52:
						{   /* back to YYINITAIL if nested comments match */
    comment_level--;
    if (comment_level == 0) yybegin(YYINITIAL); 
}
					case -53:
						break;
					case 53:
						{ comment_level++; }
					case -54:
						break;
					case 54:
						{ curr_lineno++; yybegin(YYINITIAL); }
					case -55:
						break;
					case 55:
						{ 
    String str = yytext();
    string_buf_append(str);
}
					case -56:
						break;
					case 56:
						{   /* end of string, return string buffer */
    yybegin(YYINITIAL);
    AbstractSymbol entry = AbstractTable.stringtable.addString(string_buf.toString());
    return new Symbol(TokenConstants.STR_CONST, entry);
}
					case -57:
						break;
					case 57:
						{   /* null character in string, this is an error */
    yybegin(STRING_ERROR);
    return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -58:
						break;
					case 58:
						{   /* newline without escape in string, this is an error */
    curr_lineno++;
    yybegin(YYINITIAL);
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); 
}
					case -59:
						break;
					case 59:
						{   /* any escaped character except \b\f\t\n is just the char */ 
    String str = yytext();
    string_buf_append(str.charAt(str.length() - 1));
}
					case -60:
						break;
					case 60:
						{ string_buf_append('\f'); }
					case -61:
						break;
					case 61:
						{ string_buf_append('\n'); }
					case -62:
						break;
					case 62:
						{ string_buf_append('\t'); }
					case -63:
						break;
					case 63:
						{   /* escaped null character in string, this is an error */
    yybegin(STRING_ERROR);
    return new Symbol(TokenConstants.ERROR, "String contains escaped null character");
}
					case -64:
						break;
					case 64:
						{ string_buf_append('\b'); }
					case -65:
						break;
					case 65:
						{ string_buf_append('\n');  curr_lineno++; }
					case -66:
						break;
					case 66:
						{ /* in string error state, but nothing wrong here */ }
					case -67:
						break;
					case 67:
						{   /* unterminated string reported when EOF reached,
       here only report possible too long string error. */
    yybegin(YYINITIAL);
    if (string_buf.length() >= MAX_STR_CONST) {
        return new Symbol(TokenConstants.ERROR, "String constant too long"); 
    }
}
					case -68:
						break;
					case 68:
						{   /* unescaped newline error reported whenever encountered,
       here only report possible too long string error after newline. */
    curr_lineno++;
    yybegin(YYINITIAL);
    if (string_buf.length() >= MAX_STR_CONST) {
        return new Symbol(TokenConstants.ERROR, "String constant too long"); 
    }
}
					case -69:
						break;
					case 69:
						{ /* match escaped sequence */ }
					case -70:
						break;
					case 70:
						{ /* legal newline */ curr_lineno++; }
					case -71:
						break;
					case 72:
						{ /* This rule should be the very last
                                 in your lexical specification and
                                 will match match everything not
                                 matched by other lexical rules. */
                              return new Symbol(TokenConstants.ERROR, yytext()); }
					case -72:
						break;
					case 73:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -73:
						break;
					case 74:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.FI); }
					case -75:
						break;
					case 76:
						{ return new Symbol(TokenConstants.IF); }
					case -76:
						break;
					case 77:
						{ return new Symbol(TokenConstants.IN); }
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.OF); }
					case -78:
						break;
					case 79:
						{ return new Symbol(TokenConstants.LET); }
					case -79:
						break;
					case 80:
						{ return new Symbol(TokenConstants.NEW); }
					case -80:
						break;
					case 81:
						{ return new Symbol(TokenConstants.NOT); }
					case -81:
						break;
					case 82:
						{ return new Symbol(TokenConstants.CASE); }
					case -82:
						break;
					case 83:
						{ return new Symbol(TokenConstants.LOOP); }
					case -83:
						break;
					case 84:
						{ return new Symbol(TokenConstants.ELSE); }
					case -84:
						break;
					case 85:
						{ return new Symbol(TokenConstants.ESAC); }
					case -85:
						break;
					case 86:
						{ return new Symbol(TokenConstants.THEN); }
					case -86:
						break;
					case 87:
						{ return new Symbol(TokenConstants.POOL); }
					case -87:
						break;
					case 88:
						{ return new Symbol(TokenConstants.CLASS); }
					case -88:
						break;
					case 89:
						{ return new Symbol(TokenConstants.WHILE); }
					case -89:
						break;
					case 90:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -90:
						break;
					case 91:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -91:
						break;
					case 92:
						{}
					case -92:
						break;
					case 93:
						{ 
    String str = yytext();
    string_buf_append(str);
}
					case -93:
						break;
					case 95:
						{ /* This rule should be the very last
                                 in your lexical specification and
                                 will match match everything not
                                 matched by other lexical rules. */
                              return new Symbol(TokenConstants.ERROR, yytext()); }
					case -94:
						break;
					case 96:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -95:
						break;
					case 97:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -96:
						break;
					case 98:
						{}
					case -97:
						break;
					case 100:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -98:
						break;
					case 101:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -99:
						break;
					case 102:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -100:
						break;
					case 103:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -101:
						break;
					case 104:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -102:
						break;
					case 105:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -103:
						break;
					case 106:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -104:
						break;
					case 107:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -105:
						break;
					case 108:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -106:
						break;
					case 109:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -107:
						break;
					case 110:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -108:
						break;
					case 111:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -109:
						break;
					case 112:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -110:
						break;
					case 113:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -111:
						break;
					case 114:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -112:
						break;
					case 115:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -113:
						break;
					case 116:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -114:
						break;
					case 117:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -115:
						break;
					case 118:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -116:
						break;
					case 119:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -117:
						break;
					case 120:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -118:
						break;
					case 121:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -119:
						break;
					case 122:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -120:
						break;
					case 123:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -121:
						break;
					case 124:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -122:
						break;
					case 125:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -123:
						break;
					case 126:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -124:
						break;
					case 127:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -125:
						break;
					case 128:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -126:
						break;
					case 129:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -127:
						break;
					case 130:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -128:
						break;
					case 131:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -129:
						break;
					case 132:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -130:
						break;
					case 133:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -131:
						break;
					case 134:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -132:
						break;
					case 135:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -133:
						break;
					case 136:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -134:
						break;
					case 137:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -135:
						break;
					case 138:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -136:
						break;
					case 139:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -137:
						break;
					case 140:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -138:
						break;
					case 141:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -139:
						break;
					case 142:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -140:
						break;
					case 143:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -141:
						break;
					case 144:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -142:
						break;
					case 145:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -143:
						break;
					case 146:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -144:
						break;
					case 147:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -145:
						break;
					case 148:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -146:
						break;
					case 149:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -147:
						break;
					case 150:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -148:
						break;
					case 151:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -149:
						break;
					case 152:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -150:
						break;
					case 153:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -151:
						break;
					case 154:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -152:
						break;
					case 155:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -153:
						break;
					case 156:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -154:
						break;
					case 157:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -155:
						break;
					case 158:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -156:
						break;
					case 159:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -157:
						break;
					case 160:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -158:
						break;
					case 161:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -159:
						break;
					case 162:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -160:
						break;
					case 163:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -161:
						break;
					case 164:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -162:
						break;
					case 165:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -163:
						break;
					case 166:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -164:
						break;
					case 167:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -165:
						break;
					case 168:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -166:
						break;
					case 169:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -167:
						break;
					case 170:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -168:
						break;
					case 171:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -169:
						break;
					case 172:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -170:
						break;
					case 173:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -171:
						break;
					case 174:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -172:
						break;
					case 175:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -173:
						break;
					case 176:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -174:
						break;
					case 177:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -175:
						break;
					case 178:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -176:
						break;
					case 179:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -177:
						break;
					case 180:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.TYPEID, entry);
}
					case -178:
						break;
					case 181:
						{ 
    String idString = yytext();
    AbstractSymbol entry = AbstractTable.idtable.addString(idString);                                            
    return new Symbol(TokenConstants.OBJECTID, entry);
}
					case -179:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
