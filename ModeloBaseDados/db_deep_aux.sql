-- MySQL Script generated by MySQL Workbench
-- Thu Jun 11 19:40:03 2015
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema db_deep_aux
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `db_deep_aux` ;

-- -----------------------------------------------------
-- Schema db_deep_aux
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `db_deep_aux` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `db_deep_aux` ;

-- -----------------------------------------------------
-- Table `db_deep_aux`.`Corpus`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep_aux`.`Corpus` ;

CREATE TABLE IF NOT EXISTS `db_deep_aux`.`Corpus` (
  `nome` VARCHAR(45) NOT NULL,
  `fonte` VARCHAR(45) NULL,
  `ano` VARCHAR(45) NULL,
  `genero` VARCHAR(45) NULL,
  `update` TINYINT(1) NOT NULL,
  PRIMARY KEY (`nome`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep_aux`.`Palavra`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep_aux`.`Palavra` ;

CREATE TABLE IF NOT EXISTS `db_deep_aux`.`Palavra` (
  `idPalavra` BIGINT NOT NULL AUTO_INCREMENT,
  `palavra` VARCHAR(200) NOT NULL,
  `classe` VARCHAR(45) NOT NULL,
  `categoria` VARCHAR(45) NULL,
  PRIMARY KEY (`idPalavra`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep_aux`.`Frase`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep_aux`.`Frase` ;

CREATE TABLE IF NOT EXISTS `db_deep_aux`.`Frase` (
  `idFrase` BIGINT NOT NULL,
  `frase` VARCHAR(400) NOT NULL,
  `nomeCorpus` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idFrase`),
  INDEX `fk_Frase_Corpus1_idx` (`nomeCorpus` ASC),
  CONSTRAINT `fk_Frase_Corpus1`
    FOREIGN KEY (`nomeCorpus`)
    REFERENCES `db_deep_aux`.`Corpus` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep_aux`.`Dependencia`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep_aux`.`Dependencia` ;

CREATE TABLE IF NOT EXISTS `db_deep_aux`.`Dependencia` (
  `tipoDep` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`tipoDep`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep_aux`.`Propriedade`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep_aux`.`Propriedade` ;

CREATE TABLE IF NOT EXISTS `db_deep_aux`.`Propriedade` (
  `nomeProp` VARCHAR(45) NOT NULL,
  `tipoDep` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`nomeProp`, `tipoDep`),
  INDEX `fk_Propriedade_Dependencia1_idx` (`tipoDep` ASC),
  CONSTRAINT `fk_Propriedade_Dependencia1`
    FOREIGN KEY (`tipoDep`)
    REFERENCES `db_deep_aux`.`Dependencia` (`tipoDep`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep_aux`.`Coocorrencia`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep_aux`.`Coocorrencia` ;

CREATE TABLE IF NOT EXISTS `db_deep_aux`.`Coocorrencia` (
  `idPalavra1` BIGINT NOT NULL,
  `idPalavra2` BIGINT NOT NULL,
  `nomeProp` VARCHAR(45) NOT NULL,
  `tipoDep` VARCHAR(45) NOT NULL,
  `nomeCorpus` VARCHAR(45) NOT NULL,
  `frequencia` BIGINT NOT NULL,
  `PMI` DOUBLE NULL,
  `Dice` DOUBLE NULL,
  `LogDice` DOUBLE NULL,
  `ChiPearson` DOUBLE NULL,
  `LogLikelihood` DOUBLE NULL,
  `Significance` DOUBLE NULL,
  PRIMARY KEY (`idPalavra1`, `idPalavra2`, `nomeProp`, `tipoDep`, `nomeCorpus`),
  INDEX `fk_Co-ocorrencia_Propriedade1_idx` (`nomeProp` ASC, `tipoDep` ASC),
  INDEX `fk_Co-ocorrencia_Corpus1_idx` (`nomeCorpus` ASC),
  INDEX `fk_Coocorrencia_Palavra1_idx` (`idPalavra1` ASC),
  INDEX `fk_Coocorrencia_Palavra2_idx` (`idPalavra2` ASC),
  CONSTRAINT `fk_Co-ocorrencia_Propriedade1`
    FOREIGN KEY (`nomeProp` , `tipoDep`)
    REFERENCES `db_deep_aux`.`Propriedade` (`nomeProp` , `tipoDep`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Co-ocorrencia_Corpus1`
    FOREIGN KEY (`nomeCorpus`)
    REFERENCES `db_deep_aux`.`Corpus` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Coocorrencia_Palavra1`
    FOREIGN KEY (`idPalavra1`)
    REFERENCES `db_deep_aux`.`Palavra` (`idPalavra`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Coocorrencia_Palavra2`
    FOREIGN KEY (`idPalavra2`)
    REFERENCES `db_deep_aux`.`Palavra` (`idPalavra`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep_aux`.`Exemplifica`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep_aux`.`Exemplifica` ;

CREATE TABLE IF NOT EXISTS `db_deep_aux`.`Exemplifica` (
  `idFrase` BIGINT NOT NULL,
  `idPalavra1` BIGINT NOT NULL,
  `idPalavra2` BIGINT NOT NULL,
  `nomeProp` VARCHAR(45) NOT NULL,
  `tipoDep` VARCHAR(45) NOT NULL,
  `nomeCorpus` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idFrase`, `idPalavra1`, `idPalavra2`, `nomeProp`, `tipoDep`, `nomeCorpus`),
  INDEX `fk_Exemplifica_Coocorrencia1_idx` (`idPalavra1` ASC, `idPalavra2` ASC, `nomeProp` ASC, `tipoDep` ASC, `nomeCorpus` ASC),
  CONSTRAINT `fk_Exemplifica_Frase1`
    FOREIGN KEY (`idFrase`)
    REFERENCES `db_deep_aux`.`Frase` (`idFrase`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Exemplifica_Coocorrencia1`
    FOREIGN KEY (`idPalavra1` , `idPalavra2` , `nomeProp` , `tipoDep` , `nomeCorpus`)
    REFERENCES `db_deep_aux`.`Coocorrencia` (`idPalavra1` , `idPalavra2` , `nomeProp` , `tipoDep` , `nomeCorpus`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep_aux`.`Pertence`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep_aux`.`Pertence` ;

CREATE TABLE IF NOT EXISTS `db_deep_aux`.`Pertence` (
  `idPalavra` BIGINT NOT NULL,
  `tipoDep` VARCHAR(45) NOT NULL,
  `nomeCorpus` VARCHAR(45) NOT NULL,
  `frequencia` BIGINT NOT NULL,
  PRIMARY KEY (`idPalavra`, `tipoDep`, `nomeCorpus`),
  INDEX `fk_Pertence_Corpus1_idx` (`nomeCorpus` ASC),
  INDEX `fk_Pertence_Dependencia1_idx` (`tipoDep` ASC),
  CONSTRAINT `fk_Pertence_Palavra1`
    FOREIGN KEY (`idPalavra`)
    REFERENCES `db_deep_aux`.`Palavra` (`idPalavra`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pertence_Corpus1`
    FOREIGN KEY (`nomeCorpus`)
    REFERENCES `db_deep_aux`.`Corpus` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pertence_Dependencia1`
    FOREIGN KEY (`tipoDep`)
    REFERENCES `db_deep_aux`.`Dependencia` (`tipoDep`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
