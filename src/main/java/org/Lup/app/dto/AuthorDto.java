package org.Lup.app.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

@Data
public class AuthorDto implements SQLData {
    private String name;
    private String secondName;
    private String patronymic;

    @Override
    public String getSQLTypeName() throws SQLException {
        return "author";
    }

    @Override
    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        name = stream.readString();
        secondName = stream.readString();
        patronymic = stream.readString();
    }

    @Override
    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeString(name);
        stream.writeString(secondName);
        stream.writeString(patronymic);
    }
}
