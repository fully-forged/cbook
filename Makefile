test-watch:
	DATABASE_URL="jdbc:postgresql://localhost/cbook_test?user=claudioortolina&password=" lein test-refresh
.PHONY: test-watch
