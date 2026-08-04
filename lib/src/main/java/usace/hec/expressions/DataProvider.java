package usace.hec.expressions;

import java.time.LocalDateTime;

public interface DataProvider {
    public int provideInt(String name);
    public double provideDouble(String name);
    public LocalDateTime provideDate(String name);
    public String provideString(String name);
    public boolean provideBoolean(String name);
    public void setInt(String name, int value);
    public void setDouble(String name, double value);
    public void setDate(String name, LocalDateTime value);
    public void setString(String name, String value);
    public void setBoolean(String name, boolean value);
}
