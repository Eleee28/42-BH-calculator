##########################################################

# Name of the compilation result file 
NAME     = calc.jar

# Name of the manifest, with the jar information
MANIFEST = manifest.mf

##########################################################
CYAN    =   \033[96;1m
RED     =   \033[91;1m
YELLOW  =   \033[93;1m
GREEN	=	\e[1;32m
CLEAR   =   \033[0m
##########################################################

# Source folder
SRC_FOLDER  := src

# Source files
SRC         := $(shell find $(SRC_FOLDER) -name '*.java')

# Bin files folder
BIN_FOLDER  := bin

BIN			:= $(SRC:$(SRC_FOLDER)/%.java=$(BIN_FOLDER)/%.class)

##########################################################

# Main rule, execution without params
all: $(NAME)

# Compile the jar executable
$(NAME): $(BIN)
	@echo "$(RED)[ JAR ]  $(YELLOW)Compiling $(NAME)...$(CLEAR)"
	@jar cfm $(NAME) $(MANIFEST) -C $(BIN_FOLDER) .

# Compile each java file on class file
$(BIN_FOLDER)/%.class: $(SRC_FOLDER)/%.java
	@echo "$(CYAN)[ JAVA ] $(YELLOW)Compiling $^...$(CLEAR)"
	@mkdir -p $(BIN_FOLDER)
	@javac -d $(BIN_FOLDER) -cp $(BIN_FOLDER) -sourcepath $(SRC_FOLDER) $^

##########################################################

# Rules to execute the jar
r: run

run: all
	@echo "$(GREEN)[ EXE ] Executing $(NAME)$(CLEAR)"
	@java -jar $(NAME) &

##########################################################

clean:
	@rm -rf $(BIN_FOLDER)/

fclean: clean
	@rm -rf $(NAME)

re: fclean all

.PHONY: clean fclean re r run

##########################################################

debug:
	@echo "=> $(SRC)"
	@echo "=> $(BIN)"
	@cat $(MANIFEST)
