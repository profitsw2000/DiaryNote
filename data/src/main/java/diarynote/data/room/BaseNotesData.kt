package diarynote.data.room

import diarynote.data.room.entity.NoteEntity
import java.text.SimpleDateFormat

private val formatter = SimpleDateFormat("dd.MM.yyyy")

val baseNotesList = listOf(
    NoteEntity(
        id = 0,
        category = "Без категории",
        title = "Недопустимость отказа в оказании медицинской помощи",
        text = " Отказ в оказании медицинской помощи в соответствии с программой государственных гарантий бесплатного оказания" +
                "гражданам медицинской помощи и взимание платы за ее оказание медицинской организацией участвующей в реализации этой ," +
                "программы и медицинскими работниками такой медицинской организации не допускаются",
        tags = listOf("медицина", "закон", "охрана здоровья", "323-ФЗ"),
        image = "",
        date = formatter.parse("07.05.2023"),
        edited = false,
        editDate = formatter.parse("07.05.2023"),
        userId = 0
    ),
    NoteEntity(
        id = 0,
        category = "Без категории",
        title = "Основные понятия, используемые в настоящем Федеральном законе",
        text = " охрана здоровья граждан (далее - охрана здоровья) - система мер политического, экономического, правового, социального," +
                "научного, медицинского, в том числе санитарно-противоэпидемического (профилактического), характера, осуществляемых" +
                "органами государственной власти Российской Федерации, органами государственной власти субъектов Российской Федерации," +
                "органами местного самоуправления, организациями, их должностными лицами и иными лицами, гражданами в целях" +
                "профилактики заболеваний, сохранения и укрепления физического и психического здоровья каждого человека, поддержания его" +
                "долголетней активной жизни, предоставления ему медицинской помощи",
        tags = listOf("профилактика", "заболевания", "охрана здоровья", "восстановление здоровья"),
        image = "",
        date = formatter.parse("07.05.2023"),
        edited = false,
        editDate = formatter.parse("07.05.2023"),
        userId = 0
    ),
    NoteEntity(
        id = 0,
        category = "Без категории",
        title = "Законодательство в сфере охраны здоровья",
        text = " Органы местного самоуправления в пределах своей компетенции имеют право издавать муниципальные правовые акты," +
                "содержащие нормы об охране здоровья, в соответствии с настоящим Федеральным законом, другими федеральными законами," +
                "иными нормативными правовыми актами Российской Федерации, законами и иными нормативными правовыми актами субъектов" +
                "Российской Федерации.",
        tags = listOf("медицина", "закон", "охрана здоровья", "323-ФЗ"),
        image = "",
        date = formatter.parse("07.05.2023"),
        edited = false,
        editDate = formatter.parse("07.05.2023"),
        userId = 0
    ),
    NoteEntity(
        id = 0,
        category = "Без категории",
        title = "Основные принципы охраны здоровья",
        text = "соблюдение прав граждан в сфере охраны здоровья и обеспечение связанных с этими правами государственных гарантий",
        tags = listOf("медицина", "закон", "охрана здоровья", "323-ФЗ"),
        image = "",
        date = formatter.parse("07.05.2023"),
        edited = false,
        editDate = formatter.parse("07.05.2023"),
        userId = 0
    ),
    NoteEntity(
        id = 0,
        category = "Без категории",
        title = "Соблюдение прав граждан в сфере охраны здоровья и обеспечение связанных с этими правами" +
                "государственных гарантий",
        text = " Государство обеспечивает гражданам охрану здоровья независимо от пола, расы, возраста, национальности, языка, наличия" +
                "заболеваний, состояний, происхождения, имущественного и должностного положения, места жительства, отношения к религии," +
                "убеждений, принадлежности к общественным объединениям и от других обстоятельств.",
        tags = listOf("медицина", "закон", "охрана здоровья", "323-ФЗ"),
        image = "",
        date = formatter.parse("07.05.2023"),
        edited = false,
        editDate = formatter.parse("07.05.2023"),
        userId = 0
    ),
    NoteEntity(
        id = 0,
        category = "Без категории",
        title = "Приоритет интересов пациента при оказании медицинской помощи",
        text = "создания условий, обеспечивающих возможность посещения пациента и пребывания родственников с ним в медицинской" +
                "организации с учетом состояния пациента, соблюдения противоэпидемического режима и интересов иных лиц, работающих и" +
                "(или) находящихся в медицинской организации.",
        tags = listOf("медицина", "закон", "охрана здоровья", "323-ФЗ"),
        image = "",
        date = formatter.parse("07.05.2023"),
        edited = false,
        editDate = formatter.parse("07.05.2023"),
        userId = 0
    ),
    NoteEntity(
        id = 0,
        category = "Без категории",
        title = "Приоритет охраны здоровья детей",
        text = " Органы государственной власти Российской Федерации, органы государственной власти субъектов Российской Федерации и" +
                "органы местного самоуправления в соответствии со своими полномочиями разрабатывают и реализуют программы, направленные" +
                "на профилактику, раннее выявление и лечение заболеваний, снижение материнской и младенческой смертности, формирование у" +
                "детей и их родителей мотивации к здоровому образу жизни, и принимают соответствующие меры по организации обеспечения" +
                "детей лекарственными препаратами, специализированными продуктами лечебного питания, медицинскими изделиями.",
        tags = listOf("медицина", "закон", "охрана здоровья", "323-ФЗ"),
        image = "",
        date = formatter.parse("07.05.2023"),
        edited = false,
        editDate = formatter.parse("07.05.2023"),
        userId = 0
    )
)