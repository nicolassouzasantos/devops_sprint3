create table patio (
  id bigserial primary key,
  nome varchar(255) not null,
  endereco varchar(255)
);

create table operador (
  id bigserial primary key,
  nome varchar(255) not null,
  login varchar(100) not null,
  senha varchar(255) not null
);

create table automovel (
  id bigserial primary key,
  placa varchar(20) not null,
  chassi varchar(50) not null,
  tipo varchar(100) not null,
  cor varchar(50),
  localizacao_no_patio varchar(150),
  comentarios text,
  patio_id bigint references patio(id)
);

create index idx_automovel_placa on automovel (placa);
