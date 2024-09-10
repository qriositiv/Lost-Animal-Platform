CREATE OR REPLACE FUNCTION update_report_status()
RETURNS TRIGGER AS $$
BEGIN

    IF NEW.created_at + INTERVAL '15 days' <= CURRENT_TIMESTAMP AND NEW.report_status = 'ACTIVE' THEN
        UPDATE report
        SET report_status = 'INACTIVE'
        WHERE report_id = NEW.report_id;
    END IF;


    UPDATE report
    SET report_status = 'INACTIVE'
    WHERE created_at + INTERVAL '15 days' <= CURRENT_TIMESTAMP
    AND report_status = 'ACTIVE';

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER update_report_status_trigger
AFTER INSERT ON report
FOR EACH ROW
EXECUTE FUNCTION update_report_status();
