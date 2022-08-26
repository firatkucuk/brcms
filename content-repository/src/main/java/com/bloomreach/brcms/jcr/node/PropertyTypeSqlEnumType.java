package com.bloomreach.brcms.jcr.node;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;

/** This class helps to persist, serialize, deserialize enum values into DB. */
public class PropertyTypeSqlEnumType extends EnumType {

  @Override
  public void nullSafeSet(
      final PreparedStatement st,
      final Object value,
      final int index,
      final SharedSessionContractImplementor session)
      throws HibernateException, SQLException {

    if (value == null) {
      st.setNull(index, Types.OTHER);
    } else {
      st.setObject(index, value.toString(), Types.OTHER);
    }
  }
}
