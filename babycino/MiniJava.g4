grammar MiniJava;

goal : mainClass ( classDeclaration )*;

mainClass :
    'class' identifier '{' 'public' 'static' 'void' 'main' '(' 'String' '[' ']' identifier ')' '{' statement '}' '}';

classDeclaration :
    'class' identifier ( 'extends' identifier )? '{' ( varDeclaration )* ( methodDeclaration )* '}';

varDeclaration :
    type identifier ';';

methodDeclaration :
    'public' type identifier '(' ( type identifier ( ',' type identifier )* )? ')' '{' ( varDeclaration )* ( statement )* 'return' expression ';' '}';

type :
    'int' '[' ']' # TypeIntArray
  | 'boolean'     # TypeBoolean
  | 'int'         # TypeInt
  | identifier    # TypeObject
  ;

statement :
    '{' ( statement )* '}'                             # StmtBlock
  | 'if' '(' expression ')' statement 'else' statement # StmtIf
  | 'while' '(' expression ')' statement               # StmtWhile
  | 'do' '{' statement '}' 'while' '(' expression ')' ';' # StmtDoWhile
  | 'System.out.println' '(' expression ')' ';'        # StmtPrint
  | identifier '=' expression ';'                      # StmtAssign
  | identifier '[' expression ']' '=' expression ';'   # StmtArrayAssign
  ;

expression :
    expression ( '&&' | '<' | '+' | '-' | '*' ) expression                # ExpBinOp
  | expression '[' expression ']'                                         # ExpArrayIndex
  | expression '.' 'length'                                               # ExpArrayLength
  | expression '.' identifier '(' ( expression ( ',' expression )* )? ')' # ExpMethodCall
  | INT        # ExpConstInt
  | 'true'     # ExpConstTrue
  | 'false'    # ExpConstFalse
  | identifier # ExpLocalVar
  | 'this'     # ExpThis
  | 'new' 'int' '[' expression ']' # ExpNewArray
  | 'new' identifier '(' ')'       # ExpNewObject
  | '!' expression                 # ExpNot
  | '(' expression ')'             # ExpGroup
  ;

identifier :
    IDENT;

INT : [0-9]+;

IDENT : ALPHA ALNUM*;
ALPHA : [A-Za-z_];
ALNUM : ALPHA | [0-9];

WS : [ \t\r\n]+ -> skip ;
COMMENT : '//' ~[\r\n]* -> skip ;


