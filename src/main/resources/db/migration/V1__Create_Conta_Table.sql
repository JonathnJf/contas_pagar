CREATE TABLE IF NOT EXISTS conta (
	id bigserial NOT NULL,
	data_pagamento DATE,
	data_vencimento DATE NOT NULL,
	descricao CHARACTER VARYING NOT NULL,
	situacao CHARACTER VARYING NOT NULL,
	valor NUMERIC(38,2) NOT NULL,
	PRIMARY KEY (id)
);
