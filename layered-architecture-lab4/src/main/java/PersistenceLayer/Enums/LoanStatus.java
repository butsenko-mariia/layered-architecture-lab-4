package PersistenceLayer.Enums;

public enum LoanStatus {
    ACTIVE,    // Книга зараз на руках у читача
    RETURNED,  // Книга успішно повернена
    OVERDUE, // Протермінували з терміном повернення
    DELETED // Було видалено
}
