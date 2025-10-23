-- ============================================
-- Script DDL - Sistema Financeiro
-- Database: financeiro_db
-- ============================================

-- Criar o banco de dados
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'financeiro_db')
BEGIN
    CREATE DATABASE financeiro_db;
END
GO

-- Usar o banco de dados
USE financeiro_db;
GO

-- ============================================
-- Tabela: categorias
-- ============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'categorias')
BEGIN
    CREATE TABLE categorias (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
        descricao VARCHAR(255),
        ativa BIT NOT NULL DEFAULT 1,
        CONSTRAINT UK_categorias_nome UNIQUE (nome)
    );
END
GO

-- ============================================
-- Tabela: transacoes
-- ============================================
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'transacoes')
BEGIN
    CREATE TABLE transacoes (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        descricao VARCHAR(200) NOT NULL,
        valor DECIMAL(15,2) NOT NULL,
        data DATE NOT NULL,
        tipo VARCHAR(10) NOT NULL,
        categoria_id BIGINT,
        CONSTRAINT FK_transacoes_categoria FOREIGN KEY (categoria_id)
            REFERENCES categorias(id) ON DELETE SET NULL,
        CONSTRAINT CHK_transacoes_tipo CHECK (tipo IN ('RECEITA', 'DESPESA')),
        CONSTRAINT CHK_transacoes_valor CHECK (valor >= 0)
    );
END
GO

-- ============================================
-- Índices para melhor performance
-- ============================================

-- Índice para categoria_id na tabela transacoes
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IDX_transacoes_categoria_id')
BEGIN
    CREATE INDEX IDX_transacoes_categoria_id ON transacoes(categoria_id);
END
GO

-- Índice para data na tabela transacoes
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IDX_transacoes_data')
BEGIN
    CREATE INDEX IDX_transacoes_data ON transacoes(data);
END
GO

-- Índice para tipo na tabela transacoes
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IDX_transacoes_tipo')
BEGIN
    CREATE INDEX IDX_transacoes_tipo ON transacoes(tipo);
END
GO

-- ============================================
-- Dados iniciais (opcional)
-- ============================================

-- Inserir categorias padrão
IF NOT EXISTS (SELECT * FROM categorias WHERE nome = 'Alimentação')
BEGIN
    INSERT INTO categorias (nome, descricao, ativa)
    VALUES ('Alimentação', 'Despesas com alimentação', 1);
END
GO

IF NOT EXISTS (SELECT * FROM categorias WHERE nome = 'Transporte')
BEGIN
    INSERT INTO categorias (nome, descricao, ativa)
    VALUES ('Transporte', 'Despesas com transporte', 1);
END
GO

IF NOT EXISTS (SELECT * FROM categorias WHERE nome = 'Saúde')
BEGIN
    INSERT INTO categorias (nome, descricao, ativa)
    VALUES ('Saúde', 'Despesas com saúde e medicamentos', 1);
END
GO

IF NOT EXISTS (SELECT * FROM categorias WHERE nome = 'Educação')
BEGIN
    INSERT INTO categorias (nome, descricao, ativa)
    VALUES ('Educação', 'Despesas com educação e cursos', 1);
END
GO

IF NOT EXISTS (SELECT * FROM categorias WHERE nome = 'Lazer')
BEGIN
    INSERT INTO categorias (nome, descricao, ativa)
    VALUES ('Lazer', 'Despesas com entretenimento e lazer', 1);
END
GO

IF NOT EXISTS (SELECT * FROM categorias WHERE nome = 'Salário')
BEGIN
    INSERT INTO categorias (nome, descricao, ativa)
    VALUES ('Salário', 'Receitas com salário', 1);
END
GO

-- ============================================
-- Fim do Script
-- ============================================

PRINT 'Script DDL executado com sucesso!';
PRINT 'Database: financeiro_db';
PRINT 'Tabelas criadas: categorias, transacoes';
GO

