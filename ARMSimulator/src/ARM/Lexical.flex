/* Declarations section of JFlex*/
package ARM;
import java_cup.runtime.Symbol; 

%%
%public 
%class LexicalAnalyzer //It indicates the name of the output file generated
%line //Indicates the line number where the token is located
%cup //Use it to connect with cup
%char

%init{ 
    yyline = 1; 
%init}

%{
      int direction = 0;
      int newLabel = 1; //indicates a new label
      private boolean _existTokens = true;

      public boolean thereTokens(){
        return this._existTokens;
      }

      void addLabel(String labelName, int labelDirection){
        if(newLabel == 1 && CodeGeneration.calculatingAddressesCompleted == 0){
            Label new_label = new Label(labelName,labelDirection);
            CodeGeneration.labelList.add(new_label);
        }
      }

      void lexicalError(String errorName){
        if(CodeGeneration.calculatingAddressesCompleted == 0){
          CodeGeneration.errorList.add(errorName);
          CodeGeneration.lexicalError = 1;
        }
      }
%}

%eof{
 this._existTokens = false;
%eof}

/* Regular expressions definition */

Digit = [0-9]
Number = {Digit} {Digit}*
Letter = [A-Za-z]
ANDINSTRUCTION = "AND"|"and"
EORINSTRUCTION = "EOR"|"eor"
SUBINSTRUCTION = "SUB"|"sub"
RSBINSTRUCTION = "RSB"|"rsb"
ADDINSTRUCTION = "ADD"|"add"
ADCINSTRUCTION = "ADC"|"adc"
SBCINSTRUCTION = "SBC"|"sbc"
RSCINSTRUCTION = "RSC"|"rsc"
CMPINSTRUCTION = "CMP"|"cmp"
CMNINSTRUCTION = "CMN"|"cmn"
ORRINSTRUCTION = "ORR"|"orr"
MOVINSTRUCTION = "MOV"|"mov"
LSLINSTRUCTION = "LSL"|"lsl"
LSRINSTRUCTION = "LSR"|"lsr"
ASRINSTRUCTION = "ASR"|"asr"
RRXINSTRUCTION = "RRX"|"rrx"
RORINSTRUCTION = "ROR"|"ror"
BICINSTRUCTION = "BIC"|"bic"
MVNINSTRUCTION = "MVN"|"mvn"
MULINSTRUCTION = "MUL"|"mul"
MLAINSTRUCTION = "MLA"|"mla"
STRINSTRUCTION = "STR"|"str"
LDRINSTRUCTION = "LDR"|"ldr"
STRBINSTRUCTION = "STRB"|"strb"
LDRBINSTRUCTION = "LDRB"|"ldrb"
BINSTRUCTION = "B"|"b"
BGEINSTRUCTION = "BGE"|"bge"
BLEINSTRUCTION = "BLE"|"ble"
BLTINSTRUCTION = "BLT"|"blt"
BGTINSTRUCTION = "BGT"|"bgt"
BEQINSTRUCTION = "BEQ"|"beq"
BNEINSTRUCTION = "BNE"|"bne"
BLINSTRUCTION = "BL"|"bl"
BLEQINSTRUCTION = "BLEQ"|"bleq"
BLNEINSTRUCTION = "BLNE"|"blne"
BLGEINSTRUCTION = "BLGE"|"blge"
BLLTINSTRUCTION = "BLLT"|"bllt"
BLGTINSTRUCTION = "BLGT"|"blgt"
BLLEINSTRUCTION = "BLLE"|"blle"
REGISTER_ID = "R" | "r"
//PC_ID = "PC" | "pc"
//LR_ID = "LR" | "lr"
REGISTER_NUMBER = {REGISTER_ID} {Number} | "PC" | "pc" | "LR" | "lr"
HEXADECIMAL_IMMEDIATE = ("#0x"|"#0X")[0-9|A-Fa-f]+
DECIMAL_IMMEDIATE = "#"(-?)[0-9]+
SPECIAL_CHARACTER = [<>*+.:$()'\"/;`%]
LABEL_NAME= {Letter} {Letter}*
SPACE = " "
NewLine = \n|\r|\r\n

/* End of regular expressions definition */

%%
/* End of JFlex section of declarations */
 
/* Beginning of rules section */
 
// Each rule is formed by {expresión} espacio {código}

"," {return new Symbol(sym.COMA,yyline,yychar, yytext());} 
"[" {return new Symbol(sym.CORCHETEIZQUIERDO,yyline,yychar, yytext());} 
"]" {return new Symbol(sym.CORCHETEDERECHO,yyline,yychar, yytext());} 
{ANDINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.AND,yyline,yychar,  yytext());}
{EORINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.EOR,yyline,yychar,  yytext());}
{SUBINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.SUB,yyline,yychar,  yytext());}
{RSBINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.RSB,yyline,yychar,  yytext());}
{ADDINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.ADD,yyline,yychar,  yytext());}
{ADCINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.ADC,yyline,yychar,  yytext());}
{SBCINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.SBC,yyline,yychar,  yytext());}
{RSCINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.RSC,yyline,yychar,  yytext());}
{CMPINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.CMP,yyline,yychar,  yytext());}
{CMNINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.CMN,yyline,yychar,  yytext());}
{ORRINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.ORR,yyline,yychar,  yytext());}
{MOVINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.MOV,yyline,yychar,  yytext());}
{LSLINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.LSL,yyline,yychar,  yytext());}
{LSRINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.LSR,yyline,yychar,  yytext());}
{ASRINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.ASR,yyline,yychar,  yytext());}
{RRXINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.RRX,yyline,yychar,  yytext());}
{RORINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.ROR,yyline,yychar,  yytext());}
{BICINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.BIC,yyline,yychar,  yytext());}
{MVNINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.MVN,yyline,yychar,  yytext());}
{MULINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.MUL,yyline,yychar,  yytext());}
{MLAINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.MLA,yyline,yychar,  yytext());}
{STRINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.STR,yyline,yychar,  yytext());}
{LDRINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.LDR,yyline,yychar,  yytext());}
{STRBINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.STRB,yyline,yychar,  yytext());}
{LDRBINSTRUCTION} {direction = direction + 4;
newLabel = 1;
return new Symbol(sym.LDRB,yyline,yychar,  yytext());}
{BINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.B,yyline,yychar,  yytext());}
{BGEINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.BGE,yyline,yychar,  yytext());}
{BLEINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.BLE,yyline,yychar,  yytext());}
{BLTINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.BLT,yyline,yychar,  yytext());}
{BGTINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.BGT,yyline,yychar,  yytext());}
{BEQINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.BEQ,yyline,yychar,  yytext());}
{BNEINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.BNE,yyline,yychar,  yytext());}
{BLINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.BL,yyline,yychar,  yytext());}
{BLEQINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.BLEQ,yyline,yychar,  yytext());}
{BLNEINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.BLNE,yyline,yychar,  yytext());}
{BLGEINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.BLGE,yyline,yychar,  yytext());}
{BLLTINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.BLLT,yyline,yychar,  yytext());}
{BLGTINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.BLGT,yyline,yychar,  yytext());}
{BLLEINSTRUCTION} {direction = direction + 4;
newLabel = 0;
return new Symbol(sym.BLLE,yyline,yychar,  yytext());}

{REGISTER_NUMBER} {return new Symbol(sym.REGISTER,yyline,yychar,  yytext());}
{HEXADECIMAL_IMMEDIATE} {return new Symbol(sym.HEXIMMEDIATE,yyline,yychar,  yytext());}
{DECIMAL_IMMEDIATE} {return new Symbol(sym.DECIMALIMMEDIATE,yyline,yychar,  yytext());}
//{PC_ID} {return new Symbol(sym.PC,yyline,yychar,  yytext());}
//{LR_ID} {return new Symbol(sym.LR,yyline,yychar,  yytext());}

{LABEL_NAME} {
addLabel(yytext(), direction);
newLabel = 1;
return new Symbol(sym.LABEL,yyline,yychar,  yytext());}

{SPACE} {
 // Ignorar cuando se ingrese un espacio
}

{SPECIAL_CHARACTER} {
 lexicalError("Lexical error: " + "The character " + yytext() + " is not valid." + "\n");
}

{NewLine} {
    newLabel = 1;
 // Ignorar cuando se ingrese un espacio
    return new Symbol(sym.NEWINSTRUCTION,yyline,yychar,  yytext());
}

{Number} {
 lexicalError("Lexical error on Line " + yyline + "." + "The character " + yytext() + " is not valid." + "\n");
}

