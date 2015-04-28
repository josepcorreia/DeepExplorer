-- MySQL Script generated by MySQL Workbench
-- Tue Apr 28 00:22:52 2015
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema db_deep
-- -----------------------------------------------------
-- DROP SCHEMA IF EXISTS `db_deep` ;

-- -----------------------------------------------------
-- Schema db_deep
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `db_deep` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `db_deep` ;

-- -----------------------------------------------------
-- Table `db_deep`.`Corpus`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep`.`Corpus` ;

CREATE TABLE IF NOT EXISTS `db_deep`.`Corpus` (
  `nome` VARCHAR(45) NOT NULL,
  `fonte` VARCHAR(45) NULL,
  `ano` VARCHAR(45) NULL,
  `genero` VARCHAR(45) NULL,
  `update` TINYINT(1) NOT NULL,
  PRIMARY KEY (`nome`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep`.`Palavra`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep`.`Palavra` ;

CREATE TABLE IF NOT EXISTS `db_deep`.`Palavra` (
  `idPalavra` BIGINT NOT NULL,
  `palavra` VARCHAR(45) NOT NULL,
  `classe` VARCHAR(45) NOT NULL,
  `categoria` VARCHAR(45) NULL,
  PRIMARY KEY (`idPalavra`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep`.`Frase`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep`.`Frase` ;

CREATE TABLE IF NOT EXISTS `db_deep`.`Frase` (
  `idFrase` BIGINT NOT NULL,
  `frase` VARCHAR(400) NOT NULL,
  `nomeCorpus` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idFrase`),
  INDEX `fk_Frase_Corpus1_idx` (`nomeCorpus` ASC),
  CONSTRAINT `fk_Frase_Corpus1`
    FOREIGN KEY (`nomeCorpus`)
    REFERENCES `db_deep`.`Corpus` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep`.`Dependencia`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep`.`Dependencia` ;

CREATE TABLE IF NOT EXISTS `db_deep`.`Dependencia` (
  `tipoDep` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`tipoDep`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep`.`Propriedade`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep`.`Propriedade` ;

CREATE TABLE IF NOT EXISTS `db_deep`.`Propriedade` (
  `nomeProp` VARCHAR(45) NOT NULL,
  `tipoDep` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`nomeProp`, `tipoDep`),
  INDEX `fk_Propriedade_Dependencia1_idx` (`tipoDep` ASC),
  CONSTRAINT `fk_Propriedade_Dependencia1`
    FOREIGN KEY (`tipoDep`)
    REFERENCES `db_deep`.`Dependencia` (`tipoDep`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep`.`Coocorrencia`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep`.`Coocorrencia` ;

CREATE TABLE IF NOT EXISTS `db_deep`.`Coocorrencia` (
  `idPalavra1` BIGINT NOT NULL,
  `idPalavra2` BIGINT NOT NULL,
  `nomeProp` VARCHAR(45) NOT NULL,
  `tipoDep` VARCHAR(45) NOT NULL,
  `nomeCorpus` VARCHAR(45) NOT NULL,
  `frequencia` INT NOT NULL,
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
    REFERENCES `db_deep`.`Propriedade` (`nomeProp` , `tipoDep`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Co-ocorrencia_Corpus1`
    FOREIGN KEY (`nomeCorpus`)
    REFERENCES `db_deep`.`Corpus` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Coocorrencia_Palavra1`
    FOREIGN KEY (`idPalavra1`)
    REFERENCES `db_deep`.`Palavra` (`idPalavra`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Coocorrencia_Palavra2`
    FOREIGN KEY (`idPalavra2`)
    REFERENCES `db_deep`.`Palavra` (`idPalavra`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep`.`Exemplifica`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep`.`Exemplifica` ;

CREATE TABLE IF NOT EXISTS `db_deep`.`Exemplifica` (
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
    REFERENCES `db_deep`.`Frase` (`idFrase`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Exemplifica_Coocorrencia1`
    FOREIGN KEY (`idPalavra1` , `idPalavra2` , `nomeProp` , `tipoDep` , `nomeCorpus`)
    REFERENCES `db_deep`.`Coocorrencia` (`idPalavra1` , `idPalavra2` , `nomeProp` , `tipoDep` , `nomeCorpus`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_deep`.`Pertence`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_deep`.`Pertence` ;

CREATE TABLE IF NOT EXISTS `db_deep`.`Pertence` (
  `idPalavra` BIGINT NOT NULL,
  `nomeCorpus` VARCHAR(45) NOT NULL,
  `frequencia` INT NOT NULL,
  PRIMARY KEY (`idPalavra`, `nomeCorpus`),
  INDEX `fk_Pertence_Corpus1_idx` (`nomeCorpus` ASC),
  CONSTRAINT `fk_Pertence_Palavra1`
    FOREIGN KEY (`idPalavra`)
    REFERENCES `db_deep`.`Palavra` (`idPalavra`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pertence_Corpus1`
    FOREIGN KEY (`nomeCorpus`)
    REFERENCES `db_deep`.`Corpus` (`nome`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
