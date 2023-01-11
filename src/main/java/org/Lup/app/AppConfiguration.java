package org.Lup.app;

import com.impossibl.postgres.jdbc.PGDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfiguration {

    @Bean
    public DataSource dataSource(){
        String host = "localhost";
        int port = 5432;
        String db = "library";
        String user = "postgres";
        String password = "Ytp1288";

        PGDataSource ds = new PGDataSource();
        ds.setServerName(host);
        ds.setPort(port);
        ds.setDatabaseName(db);
        ds.setUser(user);
        ds.setPassword(password);
        return ds;
    }
}
