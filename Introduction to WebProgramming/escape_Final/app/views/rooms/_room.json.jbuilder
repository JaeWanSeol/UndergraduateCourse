json.extract! room, :id, :region, :cafename, :roomname, :capacity, :theme, :level, :cafeimgurl, :roomimgurl, :addr, :etc, ,:maphtml, :created_at, :updated_at
json.url room_url(room, format: :json)
