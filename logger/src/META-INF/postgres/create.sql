CREATE OR REPLACE FUNCTION update_modified_column()
RETURN TRIGER AS $$
BEGIN
	NEW.modified = now();
	RETURN NEW;
END;

CREATE TRIGGER update_log@logger_modtime BEFORE UPDATE ON logs@logger FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
CREATE TRIGGER update_texts@logger_modtime BEFORE UPDATE ON texts@logger FOR EACH ROW EXECUTE PROCEDURE update_modified_column();
CREATE TRIGGER update_text_references@logger_modtime BEFORE UPDATE ON text_references@logger FOR EACH ROW EXECUTE PROCEDURE update_modified_column();