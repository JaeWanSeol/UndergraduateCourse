class Room < ApplicationRecord
  has_many :replies

  def self.import(file)
    spreadsheet = open_spreadsheet(file)
    header = spreadsheet.row(1)
    (2..spreadsheet.last_row).each do |i|
      row = Hash[[header, spreadsheet.row(i)].transpose]
      room = find_by_id(row["id"]) || new
      room.attributes = row.to_hash.slice(*Room.attribute_names())
      room.save!
    end
  end

  def self.open_spreadsheet(file)
    case File.extname(file.original_filename)
    when ".xlsx" then Roo::Excelx.new(file.path)
    when ".csv" then Roo::csv.new(file.path)
    else raise "Unknown file type"
    end
  end
end
