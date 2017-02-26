CREATE SEQUENCE tabla_id_seq;

CREATE TABLE public.tabla (
	id integer NOT NULL DEFAULT nextval('tabla_id_seq'::regclass),
	name character varying(254) COLLATE pg_catalog."en_US.utf8" NOT NULL,
	description character varying(254) COLLATE pg_catalog."en_US.utf8" NOT NULL,
	age character varying(254) COLLATE pg_catalog."en_US.utf8" NOT NULL,
	CONSTRAINT pk_tabla_id PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE public.tabla
  OWNER TO web;